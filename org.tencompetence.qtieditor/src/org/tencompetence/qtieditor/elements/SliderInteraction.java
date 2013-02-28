package org.tencompetence.qtieditor.elements;

import org.jdom.Attribute;
import org.jdom.Element;

public class SliderInteraction extends BlockInteraction {

	// attributes
	private int fLowerBound = 0; // required
	private int fUpperBound = 100; // required
	private int fStep = 10; // optional
	private boolean fStepLabel = false; // optional
	private String fOrientation = AssessmentElementFactory.HORIZONTAL; //optional alternative: VERTICAL

	/** Creates a new instance of SliderInteraction from UI*/
	public SliderInteraction(AssessmentItem anAssessmentItem) {
		setAssessmentItem(anAssessmentItem);		
		createResponseDeclaration();
	}
	
	/** Creates a new instance of SliderInteraction from DOM parser*/
	public SliderInteraction(AssessmentItem anAssessmentItem, String forDOM) {
		setAssessmentItem(anAssessmentItem);	
		//no need to create a response and outcome anymore
	}

	private ResponseDeclaration createResponseDeclaration(){
		ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(fAssessmentItem, "RESPONSE");
		aResponseDeclaration.setBaseType(AssessmentElementFactory.INTEGER);
		aResponseDeclaration.setCardinality(AssessmentElementFactory.SINGLE);
		setResponseDeclaration(aResponseDeclaration);
		fAssessmentItem.getResponseDeclarationList().addElement(aResponseDeclaration);
		aResponseDeclaration.setAssociatedOutcomeDeclaration(createOutcomeForExtendedText(aResponseDeclaration));
		return aResponseDeclaration;
	}
	
	public OutcomeDeclaration createOutcomeForExtendedText(ResponseDeclaration aResponseDeclaration) {
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				fAssessmentItem);
	}
	
	public void setLowerBound(int aLowerBound) {
		this.fLowerBound = aLowerBound;
	}

	public int getLowerBound() {
		return fLowerBound;
	}

	public void setUpperBound(int aUpperBound) {
		this.fUpperBound = aUpperBound;
	}

	public int getUpperBound() {
		return fUpperBound;
	}
	
	public void setStep(int i) {
		this.fStep = i;
	}

	public int getStep() {
		return fStep;
	}

	public void setStepLabel(boolean b) {
		this.fStepLabel = b;
	}

	public boolean getStepLabel() {
		return fStepLabel;
	}

	public void setOrientation(String aOrientation) {
		this.fOrientation = aOrientation;
	}

	public String getOrientation() {
		return fOrientation;
	}
	
	public void fromJDOM(Element element) {
		super.fromJDOM(element);
		
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.LOWER_BOUND.equals(tag)) {
				fLowerBound= Integer.parseInt(value);
			} else if (AssessmentElementFactory.UPPER_BOUND.equals(tag)) {
				fUpperBound= Integer.parseInt(value);
			} else if (AssessmentElementFactory.STEP.equals(tag)) {
				fStep= Integer.parseInt(value);
			} else if (AssessmentElementFactory.STEP_LABEL.equals(tag)) {
				fStepLabel = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.ORIENTATION.equals(tag)) {
				fOrientation = value;
			}
		}
	}
	
	public Element toJDOM() {
		Element aSliderInteraction = super.toJDOM();

		aSliderInteraction.setAttribute(AssessmentElementFactory.LOWER_BOUND, String.valueOf(fLowerBound));
		aSliderInteraction.setAttribute(AssessmentElementFactory.UPPER_BOUND, String.valueOf(fUpperBound));
		if (fStep > 0)
			aSliderInteraction.setAttribute(AssessmentElementFactory.STEP, String.valueOf(fStep));
		if (fStepLabel)
			aSliderInteraction.setAttribute(AssessmentElementFactory.STEP_LABEL, "true");
		if (AssessmentElementFactory.VERTICAL.equals(fOrientation))
			aSliderInteraction.setAttribute(AssessmentElementFactory.ORIENTATION, fOrientation);
		
		return aSliderInteraction;
	}
	
	@Override
	protected String getTagName() {
		return AssessmentElementFactory.SLIDER_INTERACTION;
	}

}
