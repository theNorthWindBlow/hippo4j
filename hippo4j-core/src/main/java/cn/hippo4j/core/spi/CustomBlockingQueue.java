/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hippo4j.core.spi;

import java.util.concurrent.BlockingQueue;

/**
 * Custom blockingQueue.
 *
 * @author chen.ma
 * @date 2021/7/11 00:51
 */
public interface CustomBlockingQueue {

    /**
     * Gets the custom blocking queue type.
     *
     * @return
     */
    Integer getType();

    /**
     * Adapt hippo4j core blocking queue.
     *
     * @return
     */
    default String getName() {
        return "";
    }

    /**
     * Get custom blocking queue.
     *
     * @return
     */
    BlockingQueue generateBlockingQueue();

}
