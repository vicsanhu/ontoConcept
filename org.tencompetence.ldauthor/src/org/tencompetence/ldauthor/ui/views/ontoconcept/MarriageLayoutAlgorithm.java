package org.tencompetence.ldauthor.ui.views.ontoconcept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * Reposition each node vertically so that its centerline is where its top edge was. This
 * has the effect of vertically aligning the nodes in each row along their centerlines.
 */
public class MarriageLayoutAlgorithm extends AbstractLayoutAlgorithm
{
	private Map<InternalNode, List<InternalNode>> sourcesMap;
	private Map<InternalNode, List<InternalNode>> targetsMap;

	public MarriageLayoutAlgorithm(int styles) {
		super(styles);
	}

	/**
	 * Build a mapping of InternalNode to GraphNode and back
	 */
	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height)
	{
		sourcesMap = new HashMap<InternalNode, List<InternalNode>>();
		targetsMap = new HashMap<InternalNode, List<InternalNode>>();
		for (InternalRelationship relationship : relationshipsToConsider) {
			List<InternalNode> sources = sourcesMap.get(relationship.getDestination());
			if (sources == null) {
				sources = new ArrayList<InternalNode>();
				sourcesMap.put(relationship.getDestination(), sources);
			}
			sources.add(relationship.getSource());
			List<InternalNode> targets = targetsMap.get(relationship.getSource());
			if (targets == null) {
				targets = new ArrayList<InternalNode>();
				targetsMap.put(relationship.getSource(), targets);
			}
			targets.add(relationship.getDestination());
		}
	}
	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider)
	{
		// Ignored
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return true;
	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		// Ignored
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	@Override
	protected int getCurrentLayoutStep() {
		return 0;
	}

	@Override
	protected void applyLayoutInternal(InternalNode[] arg0,
			InternalRelationship[] arg1, double arg2, double arg3, double arg4,
			double arg5) {
		// TODO Auto-generated method stub
		
	}
}
