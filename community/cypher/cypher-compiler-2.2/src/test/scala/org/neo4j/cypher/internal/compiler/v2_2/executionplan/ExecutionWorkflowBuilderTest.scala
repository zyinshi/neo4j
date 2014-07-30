/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v2_2.executionplan

import org.neo4j.cypher.ExecutionResult
import org.neo4j.cypher.internal.commons.CypherFunSuite
import org.neo4j.cypher.internal.compiler.v2_2.EagerPipeExecutionResult
import org.neo4j.cypher.internal.compiler.v2_2.pipes.Pipe
import org.neo4j.cypher.internal.compiler.v2_2.spi.QueryContext
import org.neo4j.graphdb.GraphDatabaseService
import org.mockito.Mockito._
import org.mockito.Matchers._

class ExecutionWorkflowBuilderTest extends CypherFunSuite {

  test("produces eager results for updating queries") {
    // GIVEN
    val pipe = mock[Pipe]
    when(pipe.createResults(any())).thenReturn(Iterator.empty)
    val graph = mock[GraphDatabaseService]
    val context = mock[QueryContext]
    val builderFactory = DefaultExecutionResultBuilderFactory(PipeInfo(pipe, updating = true, None), List.empty)

    // WHEN
    val builder = builderFactory.create()
    builder.setQueryContext(context)

    // THEN
    val result: ExecutionResult = builder.build(graph, "42", Map.empty)
    result shouldBe a [EagerPipeExecutionResult]
  }

  test("produces lazy results for non-updating queries") {
    // GIVEN
    val pipe = mock[Pipe]
    when(pipe.createResults(any())).thenReturn(Iterator.empty)
    val graph = mock[GraphDatabaseService]
    val context = mock[QueryContext]
    val builderFactory = DefaultExecutionResultBuilderFactory(PipeInfo(pipe, updating = false, None), List.empty)

    // WHEN
    val builder = builderFactory.create()
    builder.setQueryContext(context)

    // THEN
    val result: ExecutionResult = builder.build(graph, "42", Map.empty)
    result should not be an [EagerPipeExecutionResult]
  }
}