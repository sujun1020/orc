/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.orc.impl.reader.tree;

import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.impl.PositionProvider;
import org.apache.orc.impl.reader.StripePlanner;

import java.io.IOException;


/**
 * The top level interface that the reader uses to read the columns from the
 * ORC file.
 */
public abstract class BatchReader {
  // The row type reader
  public final TypeReader rootType;

  protected int vectorColumnCount = -1;

  public BatchReader(TypeReader rootType) {
    this.rootType = rootType;
  }

  public void startStripe(StripePlanner planner) throws IOException {
    rootType.startStripe(planner);
  }

  public void setVectorColumnCount(int vectorColumnCount) {
    this.vectorColumnCount = vectorColumnCount;
  }

  /**
   * Read the next batch of data from the file.
   * @param batch     the batch to read into
   * @param batchSize the number of rows to read
   * @throws IOException errors reading the file
   */
  public abstract void nextBatch(VectorizedRowBatch batch,
                                 int batchSize) throws IOException;

  protected void resetBatch(VectorizedRowBatch batch, int batchSize) {
    batch.selectedInUse = false;
    batch.size = batchSize;
  }

  public void skipRows(long rows) throws IOException {
    rootType.skipRows(rows);
  }

  public void seek(PositionProvider[] index) throws IOException {
    rootType.seek(index);
  }
}
