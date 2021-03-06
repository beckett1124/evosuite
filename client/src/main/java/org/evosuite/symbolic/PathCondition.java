/**
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.symbolic;

import java.util.LinkedList;
import java.util.List;

import org.evosuite.symbolic.expr.Constraint;

/**
 * Represents a sequence of branch conditions
 * 
 * @author galeotti
 *
 */
public class PathCondition {

	private final List<BranchCondition> pathCondition;

	/**
	 * Creates a new path condition from a list of branch conditions
	 * 
	 * @param branchConditions
	 */
	public PathCondition(List<BranchCondition> branchConditions) {
		this.pathCondition = new LinkedList<BranchCondition>(branchConditions);
	}

	/**
	 * Returns the constraints for this path condition
	 * 
	 * @return
	 */
	public List<Constraint<?>> getConstraints() {
		List<Constraint<?>> constraints = new LinkedList<Constraint<?>>();
		for (BranchCondition b : this.pathCondition) {
			constraints.addAll(b.getSupportingConstraints());
			constraints.add(b.getConstraint());
		}
		return constraints;
	}

	/**
	 * Creates a new path condition by negating the branch condition at index
	 * <code>branchConditionIndex</code>
	 * 
	 * @param branchConditionIndex
	 * @return
	 */
	public PathCondition negate(int branchConditionIndex) {
		if (branchConditionIndex < 0 || branchConditionIndex >= this.pathCondition.size()) {
			throw new IndexOutOfBoundsException("The position " + branchConditionIndex + " does not exists");
		}

		List<BranchCondition> newPathCondition = new LinkedList<BranchCondition>();
		for (int i = 0; i < branchConditionIndex; i++) {
			BranchCondition b = pathCondition.get(i);
			newPathCondition.add(b);
		}
		BranchCondition targetBranch = this.pathCondition.get(branchConditionIndex);
		Constraint<?> negation = targetBranch.getConstraint().negate();
		BranchCondition negatedBranch = new BranchCondition(targetBranch.getClassName(), targetBranch.getMethodName(),
				targetBranch.getBranchIndex(), negation, targetBranch.getSupportingConstraints());
		newPathCondition.add(negatedBranch);

		return new PathCondition(newPathCondition);

	}

	/**
	 * Returns the list of branch conditions on this path condition
	 * 
	 * @return
	 */
	public List<BranchCondition> getBranchConditions() {
		return this.pathCondition;
	}

	/**
	 * Returns true if the path condition is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.pathCondition.isEmpty();
	}

	/**
	 * The length of the path condition in terms of branch conditions
	 * 
	 * @return
	 */
	public int size() {
		return this.pathCondition.size();
	}

	/**
	 * Returns the branch condition at position <code>index</code>
	 * 
	 * @param index
	 * @return
	 */
	public BranchCondition get(int index) {
		return this.pathCondition.get(index);
	}
}
