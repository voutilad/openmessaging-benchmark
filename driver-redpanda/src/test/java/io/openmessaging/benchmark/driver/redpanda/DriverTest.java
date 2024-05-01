/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openmessaging.benchmark.driver.redpanda;

import org.apache.bookkeeper.stats.NullStatsLogger;
import org.apache.bookkeeper.stats.StatsLogger;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.io.IOException;
import java.nio.file.Paths;

public class DriverTest {

  private File yaml;
  private static Logger log = LoggerFactory.getLogger(DriverTest.class);

  @Before
  public void setup() {
    String path = "";
    try {
      if (!System.getProperty("driver.yaml", "").isEmpty()) {
        path = Paths.get(System.getProperty("driver.yaml")).toAbsolutePath().toString();
      } else {
        path = getClass().getClassLoader().getResource("local-redpanda.yaml").getPath();
      }
      if (path.isEmpty()) {
        Assert.fail("unable to find path for yaml file!");
      }
      yaml = new File(path);
    } catch (Exception e) {
      log.error("failed to open yaml file: {}", path);
      Assert.fail(e.getMessage());
    }
    log.info("using yaml {}", yaml.getAbsolutePath());
  }

  @Test
  public void testTopicDeletion() throws IOException {
    Assume.assumeTrue("disabled unless manually selected by setting -Dyolo",
        !System.getProperty("yolo", "").isEmpty());
    final StatsLogger statsLogger = new NullStatsLogger();
    final RedpandaBenchmarkDriver driver = new RedpandaBenchmarkDriver();

    log.info("initializing driver");
    driver.initialize(yaml, statsLogger);
  }
}
