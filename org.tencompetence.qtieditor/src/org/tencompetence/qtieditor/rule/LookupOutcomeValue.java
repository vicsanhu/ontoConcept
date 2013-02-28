package org.tencompetence.qtieditor.rule;

import org.jdom.Element;
import org.jdom.Attribute;

import org.tencompetence.qtieditor.elements.AbstractAssessment;
import org.tencompetence.qtieditor.elements.AssessmentElementFactory;
import org.tencompetence.qtieditor.elements.AssessmentItem;
import org.tencompetence.qtieditor.elements.OutcomeDeclaration;
import org.tencompetence.qtieditor.expression.Expression;
import org.tencompetence.qtieditor.expression.UndefinedExpression;

	public class LookupOutcomeValue extends AbstractRule{

		private OutcomeDeclaration fOutcomeDeclaration = null; // required
		private Expression fExpression = new UndefinedExpression(); // required
		
		public LookupOutcomeValue(AbstractAssessment aAssessment) {
			setAssessment(aAssessment);
		}
		
		public void setOutcomeDeclaration(OutcomeDeclaration aOutcomeDeclaration) {
			fOutcomeDeclaration = aOutcomeDeclaration;
		}

		public OutcomeDeclaration getOutcomeDeclaration() {
			return fOutcomeDeclaration;
		}	
		
		public void setExpression(Expression aExpression) {
			fExpression = aExpression;
		}

		public Expression getExpression() {
			return fExpression;
		}	

		public void fromJDOM(Element element) {

			for (Object attribute : element.getAttributes()) {
				String tag = ((Attribute) attribute).getName();
				String value = ((Attribute) attribute).getValue();

				if (AssessmentElementFactory.IDENTIFIER.equals(tag)) {
					setOutcomeDeclaration((OutcomeDeclaration)fAssessment.getOutcomeDeclarationByID(value));
				} 
			}

			for (Object object : element.getChildren()) {
				Element child = (Element) object;
				String tag = child.getName();
				String value = child.getText();
				
				fExpression = createExpression(child, tag, value);
			}
		}

		public Element toJDOM() {
			Element aElement = new Element(getTagName(), (fAssessment instanceof AssessmentItem)? getNamespace(): getTestNamespace());

			if (fExpression != null) {
				Element aExpressionElement = fExpression.toJDOM();
				if (aExpressionElement != null)
					aElement.addContent(aExpressionElement);
			}

			if (getOutcomeDeclaration()!=null)
				aElement.setAttribute(AssessmentElementFactory.IDENTIFIER, getOutcomeDeclaration().getId());

			return aElement;
		}
		
		public String getTagName() {
	        return AssessmentElementFactory.LOOKUP_OUTCOME_VALUE;
	    }	
	}
