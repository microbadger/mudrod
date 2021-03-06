/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.
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
package esiptestbed.mudrod.recommendation.pre;

import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import esiptestbed.mudrod.discoveryengine.DiscoveryStepAbstract;
import esiptestbed.mudrod.driver.ESDriver;
import esiptestbed.mudrod.driver.SparkDriver;
import esiptestbed.mudrod.recommendation.structure.OHCodeExtractor;

/*
 * ClassName: Generate metadata-code matrix from original metadata. Each row in
 * the matrix is corresponding to a matrix, and each column is one dimension of code.  
 */
public class OHCodeMatrixGenerator extends DiscoveryStepAbstract {

  private static final long serialVersionUID = 1L;
  private static final Logger LOG = LoggerFactory
      .getLogger(OHCodeMatrixGenerator.class);

  /**
   * Creates a new instance of OHEncodeMetadata.
   *
   * @param props
   *          the Mudrod configuration
   * @param es
   *          the Elasticsearch drive
   * @param spark
   *          the spark driver
   */
  public OHCodeMatrixGenerator(Properties props, ESDriver es,
      SparkDriver spark) {
    super(props, es, spark);
  }

  @Override

  public Object execute() {
    LOG.info(
        "*****************Metadata OHEncode matrix starts******************");
    startTime = System.currentTimeMillis();

    String metadataCodeMatrixFile = props.getProperty("metadataOBCodeMatrix");
    try {

      OHCodeExtractor extractor = new OHCodeExtractor(props);
      List<String> metedataCodes = extractor.loadMetadataOHEncode(es);

      JavaRDD<String> codeRDD = spark.sc.parallelize(metedataCodes);
      codeRDD.saveAsTextFile(props.getProperty("metadataOBCode"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    endTime = System.currentTimeMillis();
    LOG.info(
        "*****************Metadata OHEncode matrix ends******************Took {}s",
        (endTime - startTime) / 1000);
    return null;
  }

  @Override
  public Object execute(Object o) {
    return null;
  }

}
