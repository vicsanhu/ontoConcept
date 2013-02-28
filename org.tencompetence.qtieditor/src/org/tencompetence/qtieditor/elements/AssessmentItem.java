package org.tencompetence.qtieditor.elements;

import java.util.ArrayList;
import java.util.List;

import org.jdom.*;
import org.tencompetence.qtieditor.serialization.AssessmentItemSerializer;
import org.tencompetence.qtieditor.ui.AssessmentTestEditor;
import org.tencompetence.qtieditor.rule.ResponseCondition;
import org.tencompetence.qtieditor.rule.ResponseElse;
import org.tencompetence.qtieditor.rule.SetOutcomeValue;
import org.tencompetence.qtieditor.expression.BaseValue;
import org.tencompetence.qtieditor.expression.Correct;
import org.tencompetence.qtieditor.expression.IsNull;
import org.tencompetence.qtieditor.expression.MapResponse;
import org.tencompetence.qtieditor.expression.Match;
import org.tencompetence.qtieditor.expression.Variable;
import org.tencompetence.qtieditor.expression.Sum;

import org.tencompetence.ldauthor.Logger;

public class AssessmentItem extends AbstractAssessment {

	// question types
	public final static int NO = -1; // "no question is defined";
	public final static int MIXED = 0; // "mixed";
	public final static int TBMC = 1; // "text based multiple choice";
	public final static int TBMR = 2; // "text based multiple response";
	public final static int LS = 3; // "likert scale";
	public final static int YN = 4; // "Yes and No";
	public final static int OPEN = 5; // "open question";
	public final static int FIB = 6; // "filling-in-blank";
	public final static int IC = 7; // "inline choice";
	public final static int MATCH = 8; // "simple match";
	public final static int ASSOCIATE = 9; // "associate";
	public final static int ORDER = 10; // "order";
	public final static int GM = 11; // "Gap-match";
	public final static int HOTTEXT = 12; // "Gap-match";
	public final static int SLIDER = 13; // "slider";

	// attributes
	private boolean fAdaptive = false; // required, default
	private boolean fTimeDependent = false; // required, default
	private List<String> fStyleclassList = new ArrayList<String>(); // optional
	private String fLang = ""; // optional
	private String fLabel = ""; // optional

	// elements
	private BasicElementList fResponseDeclarationList;
	// private ArrayList templateDeclarationList = new ArrayList();
	// private ArrayList templateProcessingList = new ArrayList();
	private ItemBody fItemBody;
	private ResponseProcessing fResponseProcessing;
	private BasicElementList fModalFeedbackList;
	

	public AssessmentItem() {
		super.createId("AI");
		init();
	}
	
	public AssessmentItem(String id) {
		fID = id;
		init();
	}
	
	private void init() {
		fResponseDeclarationList = new BasicElementList();
		fOutcomeDeclarationList = new BasicElementList();
		fItemBody = new ItemBody(this);
		fResponseProcessing = new ResponseProcessing(this);
		fModalFeedbackList = new BasicElementList();
	}
	
	public void setAdaptive(boolean aAdaptive) {
		fAdaptive = aAdaptive;
	}

	public boolean getAdaptive() {
		return fAdaptive;
	}

	public void setTimeDependent(boolean aTimeDependent) {
		fTimeDependent = aTimeDependent;
	}

	public boolean getTimeDependent() {
		return fTimeDependent;
	}

	public void setLabel(String label) {
		this.fLabel = label;
	}

	public String getLabel() {
		return fLabel;
	}

	public void addStyleclass(String aStyleclass) {
		if (!fStyleclassList.contains(aStyleclass))
			fStyleclassList.add(aStyleclass);
	}

	public void removeStyleclass(String aStyleclass) {
		fStyleclassList.remove(aStyleclass);
	}

	public void setLang(String lang) {
		this.fLang = lang;
	}

	public String getLang() {
		return fLang;
	}
	
	public BasicElementList getResponseDeclarationList() {
		return fResponseDeclarationList;
	}

	public void addResponseDeclaration(ResponseDeclaration aResponseDeclaration) {
		fResponseDeclarationList.addElement(aResponseDeclaration);
	}

	public void removeResponseDeclaration(ResponseDeclaration aResponseDeclaration) {
		fResponseDeclarationList.removeElement(aResponseDeclaration);
	}

	public ResponseDeclaration getResponseDeclarationByID(String id) {
		ResponseDeclaration aResponseDeclaration = null;
		for (int i = 0; i < fResponseDeclarationList.size(); i++) {
			aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(i);
			if (aResponseDeclaration.getId().equals(id))
				return aResponseDeclaration;
		}
		return aResponseDeclaration;
	}

	public ResponseDeclaration getFirstResponseDeclaration() {
		if (fResponseDeclarationList.size()>0) 
			return (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
		else 
			return null;
	}
	
	public void setAssociatedOutcomeForResponse(ResponseDeclaration aResponseDeclaration) {
		
		if (aResponseDeclaration.getId().equalsIgnoreCase("RESPONSE")) {
			
			OutcomeDeclaration aOutcomeDeclaration = null;
			if ((aOutcomeDeclaration = getOutcomeDeclarationByID("SCORE"))!=null) {
				//for handling single-response interaction such as 
				//multiple choice/response, yes/no, match, associate, order, gap-match, hot-text
				aResponseDeclaration.setAssociatedOutcomeDeclaration(aOutcomeDeclaration);
			}
			else if ((aOutcomeDeclaration = getOutcomeDeclarationByID("ANSWER"))!=null){
				//for handling open question and choice interaction
				aResponseDeclaration.setAssociatedOutcomeDeclaration(aOutcomeDeclaration);
			}
			else {
				Logger.logError("AssessmentItem.setAssociatedOutcomeForResponse: no outcome 1");
			}
		}
		else {		
			OutcomeDeclaration aOutcomeDeclaration = getOutcomeDeclarationByID(aResponseDeclaration.getId()+".SCORE");
			if (aOutcomeDeclaration!=null) {
				//for handling multiple response interaction such as fill-in-the-blank and inline-choice
				aResponseDeclaration.setAssociatedOutcomeDeclaration(aOutcomeDeclaration);
			} else {
				Logger.logError("AssessmentItem.setAssociatedOutcomeForResponse: no outcome 2");
			}
		}
	}
	
	public void transformModalFeedbackToInlineFeedback() {
		
		for (int i=0; i<fModalFeedbackList.size(); i++ ) {
			ModalFeedback aModalFeedback = (ModalFeedback)fModalFeedbackList.getBasicElementAt(i);
			OutcomeDeclaration aOutcomeDeclaration = getOutcomeDeclarationByID(aModalFeedback.getOutcomeIdentifier());

			int index = aModalFeedback.getOutcomeIdentifier().lastIndexOf(".");
			ResponseDeclaration aResponseDeclaration = getResponseDeclarationByID(aModalFeedback.getOutcomeIdentifier().substring(0, index));			
			aResponseDeclaration.setFeedbackOutcomeDeclaration(aOutcomeDeclaration);
			
			Block aBlock = getItemBody().getFirstBlock();
			if (aBlock instanceof ChoiceInteraction) {
				ChoiceInteraction aChoiceInteraction = (ChoiceInteraction)aBlock;
				if (aChoiceInteraction.getResponseDeclaration() == aResponseDeclaration) {
					String aSimpleChoiceId = aModalFeedback.getId();
					for (int j=0; j<aChoiceInteraction.getSimpleChoiceList().size(); j++) {
						SimpleChoice aSimpleChoice = (SimpleChoice)aChoiceInteraction.getSimpleChoiceList().getBasicElementAt(j);
						if (aSimpleChoiceId.equalsIgnoreCase(aSimpleChoice.getId())) {
							FeedbackInline aFeedbackInline = new FeedbackInline(this, aSimpleChoice);
							aSimpleChoice.setFeedbackInline(aFeedbackInline);
							aFeedbackInline.setData(aModalFeedback.getData());
						}
					}
				}
			}
		}
	}

	public void setItemBody(ItemBody itemBody) {
		this.fItemBody = itemBody;
	}

	public ItemBody getItemBody() {
		return fItemBody;
	}

	public ResponseProcessing getResponseProcessing() {
		return fResponseProcessing;
	}

	public void setResponseProcessing(ResponseProcessing aResponseProcessing) {
		fResponseProcessing = aResponseProcessing;
	}

	public BasicElementList getModalFeedbackList() {
		return fModalFeedbackList;
	}

	public void setModalFeedbackList(BasicElementList aList) {
		fModalFeedbackList = aList;
	}

	public void addModalFeedback(ModalFeedback aModalFeedback) {
		fModalFeedbackList.addElement(aModalFeedback);
	}

	public void removeModalFeedback(ModalFeedback aModalFeedback) {
		fModalFeedbackList.removeElement(aModalFeedback);
	}
	
	
	public OutcomeDeclaration createOutcomeForMatchCorrect() {
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.INTEGER, 
				AssessmentElementFactory.SINGLE,
				this);
	}
	
	public OutcomeDeclaration createOutcomeForMapResponse() {
		return new OutcomeDeclaration("SCORE", 
				AssessmentElementFactory.FLOAT, 
				AssessmentElementFactory.SINGLE,
				this);
	}
	
	public void handleResponseProcessing(){
		fOutcomeDeclarationList.clear();
		fResponseProcessing.getResponseRuleList().clear();
		fModalFeedbackList.clear();
		
		ResponseCondition aResponseCondition = new ResponseCondition(this);
		String type = fItemBody.getQuestionClass();
        if ((type.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_CHOICE))||
        		(type.equalsIgnoreCase(AssessmentElementFactory.YES_NO)) ||
        		type.equalsIgnoreCase(AssessmentElementFactory.TEXT_BASED_MULTIPLE_RESPONSE)) {
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMapResponseTemplate(aResponseCondition, 
        			aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
    		ChoiceInteraction aChoiceInteraction = ((ChoiceInteraction)fItemBody.getFirstBlock());
    		
    		boolean result = false;
    		for (int i=0; i<aChoiceInteraction.getSimpleChoiceList().size(); i++) {
    			SimpleChoice aSimpleChoice = (SimpleChoice)aChoiceInteraction.getSimpleChoiceList().getBasicElementAt(i);
    			FeedbackInline aFeedbackInline = aSimpleChoice.getFeedbackInline();
    			if ((aFeedbackInline !=null) && (!aFeedbackInline.getData().equalsIgnoreCase(""))) {
    				result = true;
    				fModalFeedbackList.addElement(new ModalFeedback(aResponseDeclaration.getFeedbackOutcomeDeclaration().getId(),
    						aSimpleChoice.getId(),
    						AssessmentElementFactory.SHOW,
    						aFeedbackInline.getData()));
    			}
    		}
    		if (result) {
        		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
            	aSetOutcomeValue.setOutcomeDeclaration(aResponseDeclaration.getFeedbackOutcomeDeclaration());
            	Variable aVariable = new Variable(this);
        		aVariable.setVariableDeclaration(aResponseDeclaration);
            	aSetOutcomeValue.setExpression(aVariable);
            	fOutcomeDeclarationList.addElement(aResponseDeclaration.getFeedbackOutcomeDeclaration());
        		fResponseProcessing.getResponseRuleList().addElement(aSetOutcomeValue);
    			fOutcomeDeclarationList.addElement(aResponseDeclaration.getFeedbackOutcomeDeclaration());
    		}

        } else if (type.equalsIgnoreCase(AssessmentElementFactory.LIKERT)) {
    		
    		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	aSetOutcomeValue.setOutcomeDeclaration(aResponseDeclaration.getAssociatedOutcomeDeclaration());
        	Variable aVariable = new Variable(this);
    		aVariable.setVariableDeclaration(aResponseDeclaration);
        	aSetOutcomeValue.setExpression(aVariable);
        	fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aSetOutcomeValue);
        	
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.OPEN)) {
    		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	aSetOutcomeValue.setOutcomeDeclaration(aResponseDeclaration.getAssociatedOutcomeDeclaration());
        	Variable aVariable = new Variable(this);
    		aVariable.setVariableDeclaration(aResponseDeclaration);
        	aSetOutcomeValue.setExpression(aVariable);
        	fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aSetOutcomeValue);
        } else if ((type.equalsIgnoreCase(AssessmentElementFactory.FILL_IN_THE_BLANK)) ||
        		(type.equalsIgnoreCase(AssessmentElementFactory.INLINE_CHOICE))) {
    		Sum aSum = new Sum(this);    		
        	for (int i=0; i<fResponseDeclarationList.size(); i++) {
        		aResponseCondition = new ResponseCondition(this);
        		ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(i);
        		createMatchCorrectTemplate(aResponseCondition, 
        				aResponseDeclaration,
        				aResponseDeclaration.getAssociatedOutcomeDeclaration());
        		aSum.addOperand(new Variable(this, aResponseDeclaration.getAssociatedOutcomeDeclaration()));
        		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    			fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
    		}
        	if (fResponseDeclarationList.size()>0) {
        		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
        		OutcomeDeclaration aOutcomeDeclaration = createOutcomeForMatchCorrect();
        		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
        		aSetOutcomeValue.setExpression(aSum);
        		fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
        		fResponseProcessing.getResponseRuleList().addElement(aSetOutcomeValue);
        	}        	
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.MATCH)) {
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMapResponseTemplate(aResponseCondition, 
        			aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.ASSOCIATE)) {
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMapResponseTemplate(aResponseCondition, 
        			aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.ORDER)) {
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMatchCorrectTemplate(aResponseCondition, 
    				aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.GAP_MATCH)) {
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMapResponseTemplate(aResponseCondition, 
    				aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.HOT_TEXT)) {
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	if (((HottextInteraction)aResponseDeclaration.getBlockInteraction()).getMaxChoices()==1) {
        		createMatchCorrectTemplate(aResponseCondition, 
        				aResponseDeclaration,
        				aResponseDeclaration.getAssociatedOutcomeDeclaration());
        	} else {
        		createMapResponseTemplate(aResponseCondition, 
            			aResponseDeclaration,
        				aResponseDeclaration.getAssociatedOutcomeDeclaration());
        	}
       		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else if (type.equalsIgnoreCase(AssessmentElementFactory.SLIDER)) {
        	if (fResponseDeclarationList.size() == 0) return; 
        	ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(0);
        	createMapResponseTemplate(aResponseCondition, 
    				aResponseDeclaration,
    				aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fOutcomeDeclarationList.addElement(aResponseDeclaration.getAssociatedOutcomeDeclaration());
    		fResponseProcessing.getResponseRuleList().addElement(aResponseCondition);
        } else {
        	System.out.println("a unhandlable question type. perhaps misuse the interaction string as question type.");
        }
        //toDo for other type of questions
	}
	
	public void createMatchCorrectTemplate(ResponseCondition aResponseCondition, 
											ResponseDeclaration aResponseDeclaration, 
											OutcomeDeclaration aOutcomeDeclaration) {
		Match aMatch = new Match(this);
		Variable aVariable = new Variable(this);
		aVariable.setVariableDeclaration(aResponseDeclaration);
		aMatch.createEmptyOperant();
		aMatch.setFirstOperand(aVariable);
		Correct aCorrect = new Correct(this);
		aCorrect.setVariableDeclaration(aResponseDeclaration);
		aMatch.setSecondOperand(aCorrect);
		aResponseCondition.getResponseIf().setExpression(aMatch);
		
		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		BaseValue aBaseValue = new BaseValue(this);
		aBaseValue.setBaseType(AssessmentElementFactory.INTEGER);
		aBaseValue.setData("1");
		aSetOutcomeValue.setExpression(aBaseValue);
		aResponseCondition.getResponseIf().getResponseRuleList().addElement(aSetOutcomeValue);
		
		aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		aBaseValue = new BaseValue(this);
		aBaseValue.setBaseType(AssessmentElementFactory.INTEGER);
		aBaseValue.setData("0");
		aSetOutcomeValue.setExpression(aBaseValue);
		ResponseElse aResponseElse = new ResponseElse(this);
		aResponseElse.getResponseRuleList().addElement(aSetOutcomeValue);
		aResponseCondition.setResponseElse(aResponseElse);
	}
	
	public void createMapResponseTemplate(ResponseCondition aResponseCondition, 
			ResponseDeclaration aResponseDeclaration, 
			OutcomeDeclaration aOutcomeDeclaration) {
		IsNull aIsNull = new IsNull(this);
		aIsNull.createEmptyOperant();
		Variable aVariable = new Variable(this);
		aVariable.setVariableDeclaration(aResponseDeclaration);
		aIsNull.setFirstOperand(aVariable);
		aResponseCondition.getResponseIf().setExpression(aIsNull);
		
		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		BaseValue aBaseValue = new BaseValue(this);
		aBaseValue.setBaseType(AssessmentElementFactory.FLOAT);
		aBaseValue.setData("0.0");
		aSetOutcomeValue.setExpression(aBaseValue);
		aResponseCondition.getResponseIf().getResponseRuleList().addElement(aSetOutcomeValue);
		
		aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		MapResponse aMapResponse = new MapResponse(this);
		aMapResponse.setResponseDeclaration(aResponseDeclaration);
		aSetOutcomeValue.setExpression(aMapResponse);
		ResponseElse aResponseElse = new ResponseElse(this);
		aResponseElse.getResponseRuleList().addElement(aSetOutcomeValue);
		aResponseCondition.setResponseElse(aResponseElse);
	}
	
	public void createMapResponsePointTemplate(ResponseCondition aResponseCondition, 
			ResponseDeclaration aResponseDeclaration, 
			OutcomeDeclaration aOutcomeDeclaration) {
		IsNull aIsNull = new IsNull(this);
		aIsNull.createEmptyOperant();
		Variable aVariable = new Variable(this);
		aVariable.setVariableDeclaration(aResponseDeclaration);
		aIsNull.setFirstOperand(aVariable);
		aResponseCondition.getResponseIf().setExpression(aIsNull);
		
		SetOutcomeValue aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		BaseValue aBaseValue = new BaseValue(this);
		aBaseValue.setBaseType(AssessmentElementFactory.INTEGER);
		aBaseValue.setData("0");
		aSetOutcomeValue.setExpression(aBaseValue);
		aResponseCondition.getResponseIf().getResponseRuleList().addElement(aSetOutcomeValue);
		
		aSetOutcomeValue = new SetOutcomeValue(this);
		aSetOutcomeValue.setOutcomeDeclaration(aOutcomeDeclaration);
		MapResponse aMapResponse = new MapResponse(this);
		aMapResponse.setResponseDeclaration(aResponseDeclaration);
		aSetOutcomeValue.setExpression(aMapResponse);
		ResponseElse aResponseElse = new ResponseElse(this);
		aResponseElse.getResponseRuleList().addElement(aSetOutcomeValue);	
		aResponseCondition.setResponseElse(aResponseElse);
	}

	
	public void fromJDOM(Element element) {
				
		for (Object attribute : element.getAttributes()) {
			String tag = ((Attribute) attribute).getName();
			String value = ((Attribute) attribute).getValue();

			if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
				fID = value;
			} else if (AssessmentElementFactory.TITLE.equals(tag)) {
				fTitle = value;
			} else if (AssessmentElementFactory.ADAPTIVE.equals(tag)) {
				fAdaptive = "true".equalsIgnoreCase(value);
			} else if (AssessmentElementFactory.TIME_DEPENDENT.equals(tag)) {
				fTimeDependent = "true".equalsIgnoreCase(value);
			} else if(AssessmentElementFactory.TOOL_NAME.equals(tag)) {
				fToolName = value; 
			} else if(AssessmentElementFactory.TOOL_VERSION.equals(tag)) {
				fToolVersion = value; 
			}
		}

		for (Object object : element.getChildren()) {
			Element child = (Element)object;
			String tag = child.getName();
			//String value = child.getText();

			if (tag.equals(AssessmentElementFactory.OUTCOME_DECLARATION)) {
				OutcomeDeclaration aOutcomeDeclaration = new OutcomeDeclaration(this);
				aOutcomeDeclaration.fromJDOM((Element)child);
				fOutcomeDeclarationList.addElement(aOutcomeDeclaration);
			} else if (tag.equals(AssessmentElementFactory.RESPONSE_DECLARATION)) {
				ResponseDeclaration aResponseDeclaration = new ResponseDeclaration(this);
				aResponseDeclaration.fromJDOM((Element)child);
				fResponseDeclarationList.addElement(aResponseDeclaration);
				setAssociatedOutcomeForResponse(aResponseDeclaration);
			} else if (tag.equals(AssessmentElementFactory.ITEM_BODY)) {
				fItemBody.fromJDOM((Element)child);
			} else if (tag.equals(AssessmentElementFactory.RESPONSE_PROCESSING)) {
				fResponseProcessing.fromJDOM((Element)child);
			} else if (tag.equals(AssessmentElementFactory.MODAL_FEEDBACK)) {
				ModalFeedback aModalFeedback = new ModalFeedback();
				aModalFeedback.fromJDOM((Element)child);
				fModalFeedbackList.addElement(aModalFeedback);
			}
		}
		
		//change modalFeedback into simpleChoice InlineFeedback
		transformModalFeedbackToInlineFeedback();
	}

	public Element toJDOM() {
		
        Element item = new Element(getTagName(), getNamespace());
        
        AssessmentItemSerializer.handleNamespace(item);
        
        handleResponseProcessing();

		item.setAttribute(AssessmentElementFactory.IDENTIFIER, getId());
		item.setAttribute(AssessmentElementFactory.TITLE, fTitle);
		item.setAttribute(AssessmentElementFactory.ADAPTIVE, fAdaptive ? "true"
				: "false");
		item.setAttribute(AssessmentElementFactory.TIME_DEPENDENT,
				fTimeDependent ? "true" : "false");
		// item.setAttribute(AssessmentElementFactory.TOOL_NAME, fToolName);
		// item.setAttribute(AssessmentElementFactory.TOOL_VERSION,
		// fToolVersion);
		
		for (int i = 0; i < fOutcomeDeclarationList.size(); i++) {
			OutcomeDeclaration anOutcomeDeclaration = (OutcomeDeclaration)fOutcomeDeclarationList.getBasicElementAt(i);
			if (anOutcomeDeclaration==null) {
				Logger.logError("AssessmentItem.toJDOM: an OutcomeDeclaration is null");
				break;
			}
			Element outcomeDeclaration = anOutcomeDeclaration.toJDOM();
			if (outcomeDeclaration != null)
				item.addContent(outcomeDeclaration);
		}

		for (int i = 0; i < fResponseDeclarationList.size(); i++) {
			ResponseDeclaration aResponseDeclaration = (ResponseDeclaration)fResponseDeclarationList.getBasicElementAt(i);
			if (aResponseDeclaration==null) {
				Logger.logError("AssessmentItem.toJDOM: a ResponseDeclaration is null");
				break;
			}
			Element responseDeclaration = aResponseDeclaration.toJDOM();
			if (responseDeclaration != null)
				item.addContent(responseDeclaration);
		}

        Element itemBody = fItemBody.toJDOM();
        if(itemBody != null) {
            item.addContent(itemBody);
        }

        Element responseProcessing = fResponseProcessing.toJDOM();
        if(responseProcessing != null) {
            item.addContent(responseProcessing);
        }

        for (int i = 0; i < fModalFeedbackList.size(); i++) {
			Element modalFeedback = ((ModalFeedback) fModalFeedbackList
					.getBasicElementAt(i)).toJDOM();
			if (modalFeedback != null)
				item.addContent(modalFeedback);
		}
        
		return item;
	}

	public String getTagName() {
		return AssessmentElementFactory.ASSESSMENT_ITEM;
	}

	public VariableDeclaration getVariableDeclarationById(String id) {
		VariableDeclaration aVariableDeclaration = getResponseDeclarationByID(id);
		if (aVariableDeclaration != null) 
			return aVariableDeclaration;
		else 
			return getOutcomeDeclarationByID(id);
	}
}
