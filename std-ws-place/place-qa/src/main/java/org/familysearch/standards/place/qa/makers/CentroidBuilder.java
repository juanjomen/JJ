package org.familysearch.standards.place.qa.makers;

import org.familysearch.standards.place.ws.model.CentroidModel;

/**
 * Created with IntelliJ IDEA.
 * Author: Family History Department
 * User: ScholesGX
 * Date: 3/28/14
 * Time: 11:08 AM
 *
 * @copyright 3/28/14 Intellectual Reserve, Inc. All rights reserved.
 */
public class CentroidBuilder {
  private Double latitude;
  private Double longitude;

  public CentroidBuilder() {
    this.latitude = null;
    this.longitude = null;
  }

  public CentroidBuilder(CentroidModel theCentroidModel) {
    this.latitude = theCentroidModel.getLatitude();
    this.longitude = theCentroidModel.getLongitude();
  }

  public CentroidModel build() {
    CentroidModel myCentroidModel = new CentroidModel();
    myCentroidModel.setLatitude(this.latitude);
    myCentroidModel.setLongitude(this.longitude);
    return myCentroidModel;
  }

  public CentroidBuilder withLatitude(Double theLatitude) {
    if (null == theLatitude) {
      withoutLatitude();
    }
    this.latitude = theLatitude;
    return this;
  }

  public CentroidBuilder withoutLatitude() {
    this.latitude = null;
    return this;
  }

  public CentroidBuilder withLongitude(Double longitude) {
    if (null == longitude) {
      withoutLongitude();
    }
    this.longitude = longitude;
    return this;
  }

  public CentroidBuilder withoutLongitude() {
    this.longitude = null;
    return this;
  }


}
