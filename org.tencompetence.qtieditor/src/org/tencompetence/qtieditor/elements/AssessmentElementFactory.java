package org.tencompetence.qtieditor.elements;

public class AssessmentElementFactory {

	// cardinality types
	public final static String MULTIPLE = "multiple";
	public final static String ORDERED = "ordered";
	public final static String RECORD = "record";
	public final static String SINGLE = "single";
	
	//orientation
	public static final String ORIENTATION = "orientation";
	public static final String HORIZONTAL = "horizontal";
	public static final String VERTICAL = "vertical";

	// baseType
	public final static String BASE_TYPE = "baseType";
	public final static String IDENTIFIER = "identifier";
	public final static String BOOLEAN = "boolean";
	public final static String INTEGER = "integer";
	public final static String FLOAT = "float";
	public final static String STRING = "string";
	public final static String POINT = "point";
	public final static String PAIR = "pair";
	public final static String DIRECTED_PAIR = "directedPair";
	public final static String DURATION = "duration";
	public final static String FILE = "file";
	public final static String URI = "uri";

	// members of AtomicBlock
	public final static String ADDRESS = "address";
	public final static String H1 = "h1";
	public final static String H2 = "h2";
	public final static String H3 = "h3";
	public final static String H4 = "h4";
	public final static String H5 = "h5";
	public final static String H6 = "h6";
	public final static String P = "p";
	public final static String PRE = "pre";

	// members of NonAtomicBlock
	public final static String BLOCKQUOTE = "blockquote";
	public final static String DIV = "div";
	public final static String DL = "dl";
	public final static String FEEDBACK_BLOCK = "feedbackBlock";
	public final static String HR = "hr";
	public final static String MATH = "m:math";
	public final static String OL = "ol";
	public final static String POSITION_OBJECT_STAGE = "positionObjectStage";
	public final static String RUBRIC_BLOCK = "rubricBlock";
	public final static String TABLE = "table";
	public final static String TEMPLATE_BLOCK = "templateBlock";
	public final static String UL = "ul";

	// question types
	public final static String TEXT_BASED_MULTIPLE_CHOICE = "textBasedMultipleChoice";
	public final static String TEXT_BASED_MULTIPLE_RESPONSE = "textBasedMultipleResponse";
	public final static String LIKERT = "likert";
	public final static String YES_NO = "YesNo";
	public final static String OPEN = "openQuestion";
	public final static String FILL_IN_THE_BLANK = "fillInTheBlank";
	//public final static String INLINE_CHOICE = "inlineChoice";
	//public final static String MATCH = "match";
	public final static String ASSOCIATE = "associate";
	public final static String ORDER = "order";
	public final static String GAP_MATCH = "gapMatch";
	public final static String HOT_TEXT = "hotText";
	public final static String SLIDER = "slider";

	//widget title
	public static final String SOURCE_TITLE = "Source Match Set";
	public static final String TARGET_TITLE = "Target Match Set";


	// public final static String SLIDER_INTERACTION = "sliderInteraction";
	// public final static String STEP_LABEL = "stepLabel";
	public final static String ACCESS = "access";
	public final static String ADAPTIVE = "adaptive";
	public final static String AREA_MAPPING = "areaMapping";
	public final static String AREA_MAP_ENTRY = "areaMapEntry";
	public final static String ASSESSMENT_ITEM = "assessmentItem";
	public final static String ASSESSMENT_ITEM_REF = "assessmentItemRef";
	public final static String ASSESSMENT_SECTION = "assessmentSection";
	public final static String ASSESSMENT_TEST = "assessmentTest";
	public final static String ALLOW_COMMENT = "allowComment";
	public final static String ALLOW_REVIEW = "allowReview";
	public final static String ALLOW_SKIPPING = "allowSkipping";
	public final static String ASSOCIATE_INTERACTION = "associateInteraction";
	public final static String BRANCH_RULE = "branchRule";
	public final static String CARDINALITY = "cardinality";
	public final static String CATEGOTY = "category ";
	public final static String CHOICE_INTERACTION = "choiceInteraction";
	public final static String CLASS = "class";
	public final static String CORRECT_CHOICE = "correctChoice";
	public final static String CORRECT_RESPONSE = "correctResponse";
	public final static String DEFAULT_VALUE = "defaultValue";
	public final static String EXIT_RESPONSE = "exitResponse";
	public final static String EXIT_TEST = "exitTest";
	public final static String EXPECTED_LENGTH = "expectedLength";
	public final static String EXPECTED_LINES = "expectedLines";
	public final static String EXTENDED_TEXT_INTERACTION = "extendedTextInteraction";
	public final static String FEEDBACK_INLINE = "feedbackInline";
	public final static String FIXED = "fixed";
	public final static String FORMAT = "format";
	public final static String GAP = "gap";
	public final static String GAP_MATCH_INTERACTION = "gapMatchInteraction";
	public final static String GAP_TEXT = "gapText";
	public final static String HIDE = "hide";
	public final static String HOTTEXT_INTERACTION = "hottextInteraction";
	public final static String HOTTEXT = "hottext";
	public final static String HREF = "href";
	public final static String INCLUDE_BOUNDARY = "includeBoundary";
	public final static String INLINE_CHOICE = "inlineChoice";
	public final static String INLINE_CHOICE_INTERACTION = "inlineChoiceInteraction";
	public final static String INTERPOLATION_TABLE = "interpolationTable";
	public final static String INTERPOLATION_TABLE_ENTRY = "interpolationTableEntry";
	public final static String INTERPRETATION_VALUE = "interpretationValue";
	public final static String ITEM_BODY = "itemBody";
	public final static String ITEM_SESSION_CONTROL = "itemSessionControl";
	public final static String KEEP_TOGETHER = "keepTogether";
	public final static String LOOKUP_OUTCOME_VALUE = "lookupOutcomeValue";
	public final static String LOWER_BOUND = "lowerBound";
	public final static String MAP_ENTRY = "mapEntry";
	public final static String MAP_KEY = "mapKey";
	public final static String MAPPED_VALUE = "mappedValue";
	public final static String MAPPING = "mapping";
	public final static String MATCH_INTERACTION = "matchInteraction";
	public final static String MATCH_MAX = "matchMax";
	public final static String MAX_ASSOCIATIONS = "maxAssociations";
	public final static String MAX_CHOICES = "maxChoices";
	public final static String MAX_STRINGS = "maxStrings";
	public final static String MAX_TIME = "maxTime";
	public final static String MEDIA = "media";
	public final static String MIN_ASSOCIATIONS = "minAssociations";
	public final static String MIN_CHOICES = "minChoices";
	public final static String MIN_STRINGS = "minStrings";
	public final static String MIN_TIME = "minTime";
	public final static String MODAL_FEEDBACK = "modalFeedback";
	public final static String NAVIGATION_MODE = "navigationMode";
	public final static String ORDERING = "ordering";
	public final static String ORDER_INTERACTION = "orderInteraction";
	public final static String OUTCOME_CONDITION = "outcomeCondition";
	public final static String OUTCOME_DECLARATION = "outcomeDeclaration";
	public final static String OUTCOME_IDENTIFIER = "outcomeIdentifier";
	public final static String OUTCOME_IF = "outcomeIf";
	public final static String OUTCOME_ELSE = "outcomeElse";
	public final static String OUTCOME_ELSE_IF = "outcomeElseIf";
	public final static String OUTCOME_PROCESSING = "outcomeProcessing";
	public final static String PRE_CONDITION = "preCondition";
	public final static String PRINTED_VARIABLE = "printedVariable";
	public final static String PROMPT = "prompt";
	public final static String REQUIRED = "required";
	public final static String RESPONSE_CONDITION = "responseCondition";
	public final static String RESPONSE_DECLARATION = "responseDeclaration";
	public final static String RESPONSE_ELSE = "responseElse";
	public final static String RESPONSE_ELSE_IF = "responseElseIf";
	public final static String RESPONSE_IDENTIFIER = "responseIdentifier";
	public final static String RESPONSE_IF = "responseIf";
	public final static String RESPONSE_PROCESSING = "responseProcessing";
	public final static String SCORE_REPORT = "scoreReport";
	public final static String SECTION_IDENTIFIER = "sectionIdentifier";
	public final static String SELECT = "select";
	public final static String SELECTION = "selection";
	public final static String SET_OUTCOME_VALUE = "setOutcomeValue";
	public final static String SHAPE = "shape";
	public final static String SHOW = "show";
	public final static String SHOW_FEEDBACK = "showFeedback";
	public final static String SHOW_HIDE = "showHide";
	public final static String SHOW_SOLUTION = "showSolution";
	public final static String SHUFFLE = "shuffle";
	public final static String SIMPLE_ASSOCIABLE_CHOICE = "simpleAssociableChoice";
	public final static String SIMPLE_CHOICE = "simpleChoice";
	public final static String SIMPLE_MATCH_SET = "simpleMatchSet";
	public final static String SLIDER_INTERACTION = "sliderInteraction";
	public final static String SOURCE_IDENTIFIER = "sourceIdentifier";
	public final static String SOURCE_VALUE = "sourceValue";
	public final static String STEP = "step";
	public final static String STEP_LABEL = "stepLabel";
	public final static String STYLE_SHEET = "stylesheet";
	public final static String SUBMISSION_MODE = "submissionMode";
	public final static String TARGET = "target";
	public final static String TARGET_IDENTIFIER = "targetIdentifier";
	public final static String TARGET_VALUE = "targetValue";
	public final static String TEMPLATE = "template";
	public final static String TEMPLATE_DEFAULT = "templateDefault";
	public final static String TEMPLATE_IDENTIFIER = "templateIdentifier";
	public final static String TEST_FEEDBACK = "testFeedback";
	public final static String TEST_PART = "testPart";
	public final static String TIME_DEPENDENT = "timeDependent";
	public final static String TIME_LIMITS = "timeLimits";
	public final static String TITLE = "title";
	public final static String TEXT_ENTRY_INTERACTION = "textEntryInteraction";
	public final static String TOOL_NAME = "toolName";
	public final static String TOOL_VERSION = "toolVersion";
	public final static String TYPE = "type";
	public final static String UPPER_BOUND = "upperBound";
	public final static String VALIDATE_RESPONSES = "validateResponses";
	public final static String VALUE = "value";
	public final static String VARIABLE_IDENTIFIER = "variableIdentifier";
	public final static String VARIABLE_MAPPING = "variableMapping";
	public final static String VIEW = "view";
	public final static String VISIBLE = "visible";
	public final static String WEIGHT = "weight ";
	public final static String WEIGHT_IDENTIFIER = "weightIdentifier";
	public final static String WITH_REPLACEMENT = "withReplacement";

	//expression
    public final static String AND = "and";
    public final static String ANY_N = "anyN";
    public final static String BASE_VALUE = "baseValue";
    public final static String CONTAINS = "contains";
    public final static String CORRECT = "correct";
    public final static String CUSTOM_OPERATOR = "customOperator";
    public final static String DEFAULT = "default";
    public final static String DELETE = "delete";
    public final static String DIVIDE = "divide";
    public final static String DURATION_GTE = "durationGTE";
    public final static String DURATION_lt = "durationLT";
    public final static String EQUAL = "equal";
    public final static String EQUAL_ROUNDED = "equalRounded";
    public final static String FIELD_VALUE = "fieldValue";
    public final static String GT = "gt";
    public final static String GTE = "gte";
    public final static String INDEX = "index";
    public final static String INSIDE = "inside";
    public final static String INTEGER_DIVIDE = "integerDivide";
    public final static String INTEGER_MODULUS = "integerModulus";
    public final static String INTEGER_TO_FLOAT = "integerToFloat";
    public final static String IS_NULL = "isNull";
    public final static String LT = "lt";
    public final static String LTE = "lte";
    public final static String MAP_RESPONSE = "mapResponse";
    public final static String MAP_RESPONSE_POINT = "mapResponsePoint";
    public final static String MATCH = "match";
    public final static String MEMBER = "member";
    //public final static String MULTIPLE = "multiple";
    public final static String NOT = "not";
    public final static String NULL = "null";
    public final static String NUMBER_CORRECT = "numberCorrect";
    public final static String NUMBER_INCORRECT = "numberIncorrect";
    public final static String NUMBER_PRESENTED = "numberPresented";
    public final static String NUMBER_RESPONDED = "numberResponded";
    public final static String NUMBER_SELECTED = "numberSelected";
    public final static String OR = "or";
    //public final static String ORDERED = "ordered";
    public final static String PATTERN_MATCH = "patternMatch";
    public final static String POWER = "power";
    public final static String PRODUCT = "product";
    public final static String RANDOM = "random";
    public final static String RANDOM_FLOAT = "randomFloat";
    public final static String RANDOM_INTEGER = "randomInteger";
    public final static String ROUND = "round";
    public final static String STRING_MATCH = "stringMatch";
    public final static String SUBSTRING = "substring";
    public final static String SUBTRACT = "subtract";
    public final static String SUM = "sum";
    public final static String TEST_VARIABLES = "testVariables";
    public final static String TRUNCATE = "truncate";
    public final static String VARIABLE = "variable";
	public final static String UNDEFINED_EXPRESSION = "undefinedExpression";

	public final static String PROFICIENCY_LEVEL = "proficiencyLevel";
	
	/*
	public final static String SIMPLE_MATCH_SET = "simpleMatchSet";

	/*
	public static BasicElement createAssessmentElement(String name,
			AssessmentItem anAssessmentItem) {

		if (name.equals(OUTCOME_DECLARATION)) {
			return new OutcomeDeclaration(anAssessmentItem);
		} else if (name.equals(RESPONSE_DECLARATION)) {
			return new ResponseDeclaration(anAssessmentItem);
		}
		if (name.equals(ITEM_BODY)) {
			return new ItemBody(anAssessmentItem);
		} else if (name.equals(P)) {
			return new PBlock();
		} else if (name.equals(CHOICE_INTERACTION)) {
			return new ChoiceInteraction(anAssessmentItem);
		} else if (name.equals(SIMPLE_CHOICE)) {
			return new SimpleChoice(anAssessmentItem);
		} else if (name.equals(RESPONSE_PROCESSING)) {
			return new ResponseProcessing(anAssessmentItem);
		} else if (name.equals(MODAL_FEEDBACK)) {
			return new ModalFeedback(anAssessmentItem);
		}

		return null;
	}
	*/
}
