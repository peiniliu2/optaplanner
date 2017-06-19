/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.core.impl.domain.solution.descriptor;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataObject;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataArrayBasedSolution;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataSetBasedSolution;
import org.optaplanner.core.impl.testdata.domain.extended.TestdataAnnotatedExtendedSolution;
import org.optaplanner.core.impl.testdata.domain.extended.TestdataUnannotatedExtendedEntity;
import org.optaplanner.core.impl.testdata.domain.extended.abstractsolution.TestdataExtendedAbstractSolution;
import org.optaplanner.core.impl.testdata.domain.extended.abstractsolution.TestdataExtendedAbstractSolutionOverridesGetScore;
import org.optaplanner.core.impl.testdata.domain.extended.legacysolution.TestdataLegacySolution;
import org.optaplanner.core.impl.testdata.domain.reflect.generic.TestdataGenericSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.TestdataNoProblemFactPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.TestdataProblemFactPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.TestdataReadMethodProblemFactCollectionPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverFieldOverrideSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverFieldSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverGetterOverrideSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverGetterSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverGetterSubclassSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.autodiscover.TestdataAutoDiscoverUnannotatedEntitySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataDuplicatePlanningEntityCollectionPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataDuplicatePlanningScorePropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataDuplicateProblemFactCollectionPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataProblemFactCollectionPropertyWithArgumentSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataProblemFactIsPlanningEntityCollectionPropertySolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataUnknownFactTypeSolution;
import org.optaplanner.core.impl.testdata.domain.solutionproperties.invalid.TestdataUnsupportedFactTypeSolution;
import org.optaplanner.core.impl.testdata.util.PlannerTestUtils;
import org.optaplanner.core.impl.testdata.util.TestdataCodeAssertableArrayList;

import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;

public class SolutionDescriptorTest {

    // ************************************************************************
    // Problem fact and planning entity properties
    // ************************************************************************

    @Test
    public void problemFactProperty() {
        SolutionDescriptor<TestdataProblemFactPropertySolution> solutionDescriptor
                = TestdataProblemFactPropertySolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(),
                "extraObject");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "valueList", "otherProblemFactList");
    }

    @Test
    public void readMethodProblemFactCollectionProperty() {
        SolutionDescriptor<TestdataReadMethodProblemFactCollectionPropertySolution> solutionDescriptor
                = TestdataReadMethodProblemFactCollectionPropertySolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "valueList", "createProblemFacts");
    }

    @Test(expected = IllegalStateException.class)
    public void problemFactCollectionPropertyWithArgument() {
        TestdataProblemFactCollectionPropertyWithArgumentSolution.buildSolutionDescriptor();
    }

    @Test(expected = IllegalStateException.class)
    public void duplicateProblemFactCollectionProperty() {
        TestdataDuplicateProblemFactCollectionPropertySolution.buildSolutionDescriptor();
    }

    @Test(expected = IllegalStateException.class)
    public void duplicatePlanningEntityCollectionProperty() {
        TestdataDuplicatePlanningEntityCollectionPropertySolution.buildSolutionDescriptor();
    }

    @Test(expected = IllegalStateException.class)
    public void duplicatePlanningScorePropertyProperty() {
        TestdataDuplicatePlanningScorePropertySolution.buildSolutionDescriptor();
    }

    @Test(expected = IllegalStateException.class)
    public void problemFactIsPlanningEntityCollectionProperty() {
        TestdataProblemFactIsPlanningEntityCollectionPropertySolution.buildSolutionDescriptor();
    }

    @Test
    public void noProblemFactPropertyWithEasyScoreCalculation() {
        SolverFactory<TestdataNoProblemFactPropertySolution> solverFactory
                = PlannerTestUtils.buildSolverFactory(
                        TestdataNoProblemFactPropertySolution.class, TestdataEntity.class);
        solverFactory.buildSolver();
    }

    @Test(expected = IllegalStateException.class)
    public void noProblemFactPropertyWithDroolsScoreCalculation() {
        SolverFactory<TestdataNoProblemFactPropertySolution> solverFactory
                = PlannerTestUtils.buildSolverFactoryWithDroolsScoreDirector(
                        TestdataNoProblemFactPropertySolution.class, TestdataEntity.class);
        solverFactory.buildSolver();
    }

    @Test
    public void extended() {
        SolutionDescriptor<TestdataAnnotatedExtendedSolution> solutionDescriptor
                = TestdataAnnotatedExtendedSolution.buildExtendedSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "valueList", "subValueList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList", "subEntityList");
    }

    @Test
    public void setProperties() {
        SolutionDescriptor<TestdataSetBasedSolution> solutionDescriptor
                = TestdataSetBasedSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "valueSet");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entitySet");
    }

    @Test @Ignore("Resolve PLANNER-573 to fix this")
    public void arrayProperties() {
        SolutionDescriptor<TestdataArrayBasedSolution> solutionDescriptor
                = TestdataArrayBasedSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "values");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entities");
    }

    @Test
    public void generic() {
        SolutionDescriptor<TestdataGenericSolution> solutionDescriptor
                = TestdataGenericSolution.buildSolutionDescriptor();
    }

    // ************************************************************************
    // Inheritance
    // ************************************************************************

    @Test
    public void extendedAbstractSolution() {
        SolutionDescriptor<TestdataExtendedAbstractSolution> solutionDescriptor
                = TestdataExtendedAbstractSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataExtendedAbstractSolution solution = new TestdataExtendedAbstractSolution();
        solution.setValueList(Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2")));
        solution.setExtraObject(new TestdataValue("extra"));
        solution.setEntityList(Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2")));

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "e1", "e2", "v1", "v2", "extra");
    }

    @Test
    public void extendedAbstractSolutionOverridesGetScore() {
        SolutionDescriptor<TestdataExtendedAbstractSolutionOverridesGetScore> solutionDescriptor
                = TestdataExtendedAbstractSolutionOverridesGetScore.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataExtendedAbstractSolutionOverridesGetScore solution = new TestdataExtendedAbstractSolutionOverridesGetScore();
        solution.setValueList(Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2")));
        solution.setExtraObject(new TestdataValue("extra"));
        solution.setEntityList(Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2")));

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "e1", "e2", "v1", "v2", "extra");
    }

    // ************************************************************************
    // Autodiscovery
    // ************************************************************************

    @Test(expected = IllegalArgumentException.class)
    public void autoDiscoverProblemFactCollectionPropertyElementTypeUnsupported() {
        TestdataUnsupportedFactTypeSolution.buildSolutionDescriptor();
    }

    @Test(expected = IllegalArgumentException.class)
    public void autoDiscoverProblemFactCollectionPropertyElementTypeUnknown() {
        TestdataUnknownFactTypeSolution.buildSolutionDescriptor();
    }

    @Test
    public void autoDiscoverFields() {
        SolutionDescriptor<TestdataAutoDiscoverFieldSolution> solutionDescriptor
                = TestdataAutoDiscoverFieldSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(), "singleProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2"));
        List<TestdataEntity> entityList = Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2"));
        TestdataEntity otherEntity = new TestdataEntity("otherE1");
        TestdataAutoDiscoverFieldSolution solution = new TestdataAutoDiscoverFieldSolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "otherE1", "p1", "e1", "e2", "v1", "v2");
    }

    @Test
    public void autoDiscoverGetters() {
        SolutionDescriptor<TestdataAutoDiscoverGetterSolution> solutionDescriptor
                = TestdataAutoDiscoverGetterSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(), "singleProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2"));
        List<TestdataEntity> entityList = Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2"));
        TestdataEntity otherEntity = new TestdataEntity("otherE1");
        TestdataAutoDiscoverGetterSolution solution = new TestdataAutoDiscoverGetterSolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "otherE1", "p1", "e1", "e2", "v1", "v2");
    }

    @Test
    public void autoDiscoverFieldsFactCollectionOverridenToSingleProperty() {
        SolutionDescriptor<TestdataAutoDiscoverFieldOverrideSolution> solutionDescriptor
                = TestdataAutoDiscoverFieldOverrideSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(),
                "singleProblemFact", "listProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2"));
        List<TestdataEntity> entityList = Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2"));
        TestdataEntity otherEntity = new TestdataEntity("otherE1");
        List<String> listFact = new TestdataCodeAssertableArrayList<>("list1", Arrays.asList("x", "y"));
        TestdataAutoDiscoverFieldOverrideSolution solution = new TestdataAutoDiscoverFieldOverrideSolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity, listFact);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution),
                "otherE1", "list1", "p1", "e1", "e2", "v1", "v2");
    }

    @Test
    public void autoDiscoverGettersFactCollectionOverridenToSingleProperty() {
        SolutionDescriptor<TestdataAutoDiscoverGetterOverrideSolution> solutionDescriptor
                = TestdataAutoDiscoverGetterOverrideSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(),
                "singleProblemFact", "listProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2"));
        List<TestdataEntity> entityList = Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2"));
        TestdataEntity otherEntity = new TestdataEntity("otherE1");
        List<String> listFact = new TestdataCodeAssertableArrayList<>("list1", Arrays.asList("x", "y"));
        TestdataAutoDiscoverGetterOverrideSolution solution = new TestdataAutoDiscoverGetterOverrideSolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity, listFact);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution),
                "otherE1", "list1", "p1", "e1", "e2", "v1", "v2");
    }

    @Test
    public void autoDiscoverUnannotatedEntitySubclass() {
        SolutionDescriptor<TestdataAutoDiscoverUnannotatedEntitySolution> solutionDescriptor
                = TestdataAutoDiscoverUnannotatedEntitySolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(), "singleProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2"));
        List<TestdataUnannotatedExtendedEntity> entityList = Arrays.asList(
                new TestdataUnannotatedExtendedEntity("u1"),
                new TestdataUnannotatedExtendedEntity("u2"));
        TestdataUnannotatedExtendedEntity otherEntity = new TestdataUnannotatedExtendedEntity("otherU1");
        TestdataAutoDiscoverUnannotatedEntitySolution solution = new TestdataAutoDiscoverUnannotatedEntitySolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "otherU1", "p1", "u1", "u2", "v1", "v2");
    }

    @Test @Ignore("Ignore to fix the build - but this is a BUG I'll fix now")
    public void autoDiscoverGettersOverriddenInSubclass() {
        SolutionDescriptor<TestdataAutoDiscoverGetterSolution> solutionDescriptor
                = TestdataAutoDiscoverGetterSolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap(), "singleProblemFact");
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFactList");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap(), "otherEntity");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");

        TestdataObject singleProblemFact = new TestdataObject("p1");
        List<TestdataValue> valueList = new TestdataCodeAssertableArrayList<>(
                "L1", Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2")));
        List<TestdataEntity> entityList = Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2"));
        TestdataEntity otherEntity = new TestdataEntity("otherE1");
        TestdataAutoDiscoverGetterSubclassSolution solution = new TestdataAutoDiscoverGetterSubclassSolution(
                "s1", singleProblemFact, valueList, entityList, otherEntity);

        assertAllCodesOfCollection(solutionDescriptor.getAllFacts(solution), "otherE1", "p1", "e1", "e2", "L1");
    }

    // ************************************************************************
    // Legacy
    // ************************************************************************

    @Test @Deprecated
    public void legacySolution() {
        SolutionDescriptor<TestdataLegacySolution> solutionDescriptor
                = TestdataLegacySolution.buildSolutionDescriptor();
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getProblemFactCollectionMemberAccessorMap(),
                "problemFacts");
        assertMapContainsKeysExactly(solutionDescriptor.getEntityMemberAccessorMap());
        assertMapContainsKeysExactly(solutionDescriptor.getEntityCollectionMemberAccessorMap(),
                "entityList");
    }

}
