package org.tencompetence.qtieditor.elements;


import org.jdom.Attribute;
import org.jdom.Element;


public class ResponseDeclaration extends VariableDeclaration {

	private InterpretationValue DefaultValue = new InterpretationValue(AssessmentElementFactory.DEFAULT_VALUE);
	private InterpretationValue fCorrectResponse = new InterpretationValue(AssessmentElementFactory.CORRECT_RESPONSE);
	private Mapping fMapping = new Mapping();
	private AreaMapping fAreaMapping = new AreaMapping();

	private BlockInteraction fBlockInteraction = null;

	private AssessmentItem fAssessmentItem;
	private OutcomeDeclaration fAssociatedOutcomeDeclaration = null;
	private OutcomeDeclaration fFeedbackOutcomeDeclaration = null;

	/** Creates a new instance of ResponseDeclaration */
	public ResponseDeclaration(AssessmentItem anAssessmentItem) {
		super.createId("RD");
		fAssessmentItem = anAssessmentItem;
	}

	public ResponseDeclaration(AssessmentItem anAssessmentItem, String id) {
		setId(id);
		fAssessmentItem = anAssessmentItem;
	}

	public AssessmentItem getAssessmentItem() {
		return fAssessmentItem;
	}

	public ResponseDeclaration(BlockInteraction anInteraction) {
		setBlockInteraction(anInteraction);
	}

	/*
	 * public ResponseDeclaration(String responseId, String cardinality, String
	 * baseType, InterpretationValue defaultValue, InterpretationValue
	 * correctResponse, Mapping mapping, AreaMapping areaMapping) {
	 * setId(responseId); this.cardinality = cardinality; this.baseType =
	 * baseType; this.defaultValue = defaultValue; this.correctResponse =
	 * correctResponse; this.mapping = mapping; this.areaMapping = areaMapping; }
	 */

	public void setBlockInteraction(BlockInteraction blockInteraction) {
		fBlockInteraction = blockInteraction;
	}

	public BlockInteraction getBlockInteraction() {
		return fBlockInteraction;
	}

	public void setDefaultValue(InterpretationValue aDefaultValue) {
		DefaultValue = aDefaultValue;
	}

	public InterpretationValue getDefaultValue() {
		return DefaultValue;
	}

	public void setCorrectResponse(InterpretationValue aCorrectResponse) {
		fCorrectResponse = aCorrectResponse;
	}

	public InterpretationValue getCorrectResponse() {
		return fCorrectResponse;
	}
	
	public void setFirstCorrectResponse(String baseType, String data) {
		
		Value aValue = new Value(baseType, data);
		if (fCorrectResponse.size()==0)
			fCorrectResponse.addValue(aValue);
		else
			fCorrectResponse.setValueAt(0, aValue);
	}

	public Value getFirstCorrectResponseValue() {
		 return fCorrectResponse.getValueAt(0);
	}

	public String getFirstCorrectResponse() {
		if (fCorrectResponse.getValueAt(0)!=null)
			return fCorrectResponse.getValueAt(0).getData();
		else
			return new String("");
	}
	
	public void setMapping(Mapping mapping) {
		fMapping = mapping;
	}

	public Mapping getMapping() {
		return fMapping;
	}

	public void setAreaMapping(AreaMapping areaMapping) {
		fAreaMapping = areaMapping;
	}

	public AreaMapping getAreaMapping() {
		return fAreaMapping;
	}
	
	public void setAssociatedOutcomeDeclaration(OutcomeDeclaration aAssociatedOutcomeDeclaration) {
		fAssociatedOutcomeDeclaration = aAssociatedOutcomeDeclaration;
	}

	public OutcomeDeclaration getAssociatedOutcomeDeclaration() {
		return fAssociatedOutcomeDeclaration;
	}

	public void setFeedbackOutcomeDeclaration(OutcomeDeclaration aFeedbackOutcomeDeclaration) {
		fFeedbackOutcomeDeclaration = aFeedbackOutcomeDeclaration;
	}

	public OutcomeDeclaration getFeedbackOutcomeDeclaration() {
		return fFeedbackOutcomeDeclaration;
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);
				
		for (Object o : element.getChildren()) {
			Element child = (Element) o;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.DEFAULT_VALUE)) {
				DefaultValue.fromJDOM((Element)child);
			} 
			else if (tag.equals(AssessmentElementFactory.CORRECT_RESPONSE)) {
				fCorrectResponse.fromJDOM((Element)child);
			} 
			else if (tag.equals(AssessmentElementFactory.MAPPING)) {
				fMapping.fromJDOM((Element)child);
			} 
		}

	}
	
	public Element toJDOM() {			
		Element aResponseDeclaration = new Element(getTagName(), getNamespace());

		aResponseDeclaration.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		aResponseDeclaration.setAttribute(AssessmentElementFactory.CARDINALITY, fCardinality);
		aResponseDeclaration.setAttribute(AssessmentElementFactory.BASE_TYPE, fBaseType);	
		
		Element aDefaultValue = DefaultValue.toJDOM();
        if(aDefaultValue != null) {
        	aResponseDeclaration.addContent(aDefaultValue);
        }

        Element aCorrectResponse = fCorrectResponse.toJDOM();
		if(aCorrectResponse != null) 
			aResponseDeclaration.addContent(aCorrectResponse);
		
		Element aMappingElement = fMapping.toJDOM();
		if(aMappingElement != null) {
 	       aResponseDeclaration.addContent(aMappingElement);
 		}

		return aResponseDeclaration;
	}

	public String getTagName() {
		return AssessmentElementFactory.RESPONSE_DECLARATION;
	}

}
