/**
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */

package com.yahoo.ycsb.generator;

/**
 * Generate a popularity distribution of items, skewed to favor recent items significantly more than older items.
 */
public class SkewedLatestGenerator extends LongGenerator {
    CounterGenerator _basis;
    ZipfianGenerator _zipfian;

    public SkewedLatestGenerator(CounterGenerator basis) {
        _basis = basis;
        _zipfian = new ZipfianGenerator(_basis.lastLong());
        nextLong();
    }

    public static void main(String[] args) {
        SkewedLatestGenerator gen = new SkewedLatestGenerator(new CounterGenerator(1000));
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            System.out.println(gen.nextString());
        }

    }

    /**
     * Generate the next string in the distribution, skewed Zipfian favoring the items most recently returned by the basis generator.
     */
    public long nextLong() {
        long max = _basis.lastLong();
        long nextlong = max - _zipfian.nextLong(max+1); //+1 because range shoudl be start to end-1 (insertend is exclusive)
        setLastLong(nextlong);
        return nextlong;
    }

    @Override
    public double mean() {
        throw new UnsupportedOperationException("Can't compute mean of non-stationary distribution!");
    }

}
