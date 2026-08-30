package com.collabnet.ccf.ccfmaster.server.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResult;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.ConversionResultFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingExternalAppTemplatePersisterFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingLandscapeTemplatePersisterFactory;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.FieldMappingPersisterFactory;

public aspect FieldMappingPersistingAspect {

	private interface CanPersistToXsl {}

	/*
	 * conversionResultFactory used to be a single inter-type field declared on
	 * CanPersistToXsl:
	 *
	 *     @Autowired
	 *     private transient ConversionResultFactory CanPersistToXsl.conversionResultFactory;
	 *     public ConversionResultFactory CanPersistToXsl.getConversionResultFactory() { ... }
	 *
	 * AspectJ 1.9.24 and 1.9.25 cannot compile an inter-type *field* declaration whose
	 * target type is an interface: ajc aborts with
	 *     java.lang.AssertionError: Attempt to push null on operand stack!
	 *     at ...codegen.OperandStack.push(OperandStack.java:58)
	 *     at ...ast.InterTypeFieldDeclaration.generateInterfaceWriteBody(...:417)
	 * at every compliance level. 1.9.22.1 compiles it, but 1.9.22.1 cannot target Java
	 * 25 (it rejects -25), and 1.9.25 is the first release that can - so the two
	 * requirements are mutually exclusive and this one construct had to go.
	 *
	 * The field is therefore declared once per implementing type instead. ajc already
	 * materialised a per-class field for the interface ITD, and every read of it in this
	 * aspect is through a concrete type, so this is the same code with the same
	 * @Autowired injection. The marker interface and its declare-parents are kept so the
	 * three entities' type hierarchies are unchanged; only getConversionResultFactory,
	 * which nothing outside this aspect ever called, moves off the interface.
	 */
	@Autowired
	private transient ConversionResultFactory FieldMapping.conversionResultFactory;
	public ConversionResultFactory FieldMapping.getConversionResultFactory() {
		return conversionResultFactory;
	}

	@Autowired
	private transient ConversionResultFactory FieldMappingLandscapeTemplate.conversionResultFactory;
	public ConversionResultFactory FieldMappingLandscapeTemplate.getConversionResultFactory() {
		return conversionResultFactory;
	}

	@Autowired
	private transient ConversionResultFactory FieldMappingExternalAppTemplate.conversionResultFactory;
	public ConversionResultFactory FieldMappingExternalAppTemplate.getConversionResultFactory() {
		return conversionResultFactory;
	}

	declare parents: FieldMapping implements CanPersistToXsl;
	
//	@Autowired
//	public transient FieldMappingPersisterFactory CanPersistToXsl.persisterFactory;
//	@Autowired
//	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingExternalAppTemplate.persisterFactory;
//
//	@Autowired
//	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingLandscapeTemplate.persisterFactory;

	@Autowired
	private transient FieldMappingPersisterFactory FieldMapping.persisterFactory;
	
	public FieldMappingPersisterFactory FieldMapping.getPersisterFactory() {
		return persisterFactory;
	}
	
	public void FieldMapping.setPersisterFactory(FieldMappingPersisterFactory factory) {
		persisterFactory = factory;
	}
	
	void around(FieldMapping cfg) : execution(void FieldMapping.persist()) && target(cfg) {
		proceed(cfg);
		if (cfg.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION) {
			ConversionResult cr = cfg.conversionResultFactory.get(cfg);
			Persister<FieldMapping> persister = cfg.persisterFactory.get(cr);
			persister.save(cfg);
		}
	}
	FieldMapping around(FieldMapping cfg) : execution(FieldMapping FieldMapping.merge()) && target(cfg) {
		FieldMapping result = proceed(cfg);
		if (result.getScope() == FieldMappingScope.REPOSITORY_MAPPING_DIRECTION) {
			ConversionResult cr = result.conversionResultFactory.get(result);
			Persister<FieldMapping> persister = result.persisterFactory.get(cr);
			persister.save(result);
		}
		return result;
	}

	@Autowired
	private transient FieldMappingLandscapeTemplatePersisterFactory FieldMappingLandscapeTemplate.persisterFactory;
	public void FieldMappingLandscapeTemplate.setPersisterFactory(FieldMappingLandscapeTemplatePersisterFactory fmltpf) {
		persisterFactory = fmltpf;
	}
	@Autowired
	private transient FieldMappingExternalAppTemplatePersisterFactory FieldMappingExternalAppTemplate.persisterFactory;
	public void FieldMappingExternalAppTemplate.setPersisterFactory(FieldMappingExternalAppTemplatePersisterFactory fmeatpf) {
		persisterFactory = fmeatpf;
	}
	
	declare parents: FieldMappingLandscapeTemplate implements CanPersistToXsl;
	declare parents: FieldMappingExternalAppTemplate implements CanPersistToXsl;
	
	void around(FieldMappingLandscapeTemplate template) : execution(void FieldMappingLandscapeTemplate.persist()) && target(template) {
		proceed(template);
		ConversionResult cr = template.conversionResultFactory.get(template);
		Persister<FieldMappingLandscapeTemplate> persister = template.persisterFactory.get(cr);
		persister.save(template);
	}
	FieldMappingLandscapeTemplate around(FieldMappingLandscapeTemplate template) : execution(FieldMappingLandscapeTemplate FieldMappingLandscapeTemplate.merge()) && target(template) {
		FieldMappingLandscapeTemplate result = proceed(template);
		ConversionResult cr = result.conversionResultFactory.get(result);
		Persister<FieldMappingLandscapeTemplate> persister = result.persisterFactory.get(cr);
		persister.save(result);
		return result;
	}
	
	void around(FieldMappingExternalAppTemplate template) : execution(void FieldMappingExternalAppTemplate.persist()) && target(template) {
		proceed(template);
		ConversionResult cr = template.conversionResultFactory.get(template);
		Persister<FieldMappingExternalAppTemplate> persister = template.persisterFactory.get(cr);
		persister.save(template);
	}
	FieldMappingExternalAppTemplate around(FieldMappingExternalAppTemplate template) : execution(FieldMappingExternalAppTemplate FieldMappingExternalAppTemplate.merge()) && target(template) {
		FieldMappingExternalAppTemplate result = proceed(template);
		ConversionResult cr = result.conversionResultFactory.get(result);
		Persister<FieldMappingExternalAppTemplate> persister = result.persisterFactory.get(cr);
		persister.save(result);
		return result;
	}
}
