/**
 * 
 */
package com.training.guice.validation.engine.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.training.guice.validation.engine.Rule;
import com.training.guice.validation.engine.RulesCollection;
import com.training.guice.validation.engine.ValidatableObject;
import com.training.guice.validation.engine.ValidatableOperation;

/**
 * @author Mina
 *
 */
public abstract class AbstractRulesCollection<Subject extends ValidatableObject> implements RulesCollection<Subject> {

	private final Map<Class<? extends ValidatableOperation>, Set<Rule<Subject>>> rules = new HashMap<Class<? extends ValidatableOperation>, Set<Rule<Subject>>>();

	/**
	 * 
	 */
	public AbstractRulesCollection() {
		buildRuleSet();
	}

	protected abstract void buildRuleSet();

	@Override
	public void applyRules(Subject object, Class<? extends ValidatableOperation> operation) {
		rules.get(operation).forEach(rule -> rule.execute(object));
	}

	protected void registerRuleSet(Class<? extends ValidatableOperation> operation, Set<Rule<Subject>> ruleSet) {
		rules.put(operation, ruleSet);
	}
}
