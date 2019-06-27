package aws.fillspec;


import com.google.common.collect.ImmutableSet;
import java.util.Collection;

/**
 * Generated values of product markets.
 * Note that RDW and MRM are the same.
 *
 * @since MIB3OI_Products_V2.4.xlsx
 */
public enum ProductMarket
{
  UNDEFINED(ProductMarketType.UNDEFINED, "UNDEFINED", ImmutableSet.of(-1)),
  AGCC(ProductMarketType.MRM, "AGCC", ImmutableSet.of(52, 72, 26, 92, 13, 30)),
  ASIA(ProductMarketType.MRM, "ASIA", ImmutableSet.of(53, 9, 73, 10, 93, 30)),
  AUS(ProductMarketType.MRM, "AUSTRALIA", ImmutableSet.of(54, 7, 8, 74, 30, 94)),
  CHL(ProductMarketType.MRM, "CHILE", ImmutableSet.of(55, 23, 24, 75, 30, 95)),
  EUR(ProductMarketType.EU, "EUROPE", ImmutableSet.of(2, 50, 66, 82, 70, 90, 106, 28, 29, 14, 30)),
  INDIA(ProductMarketType.MRM, "INDIA", ImmutableSet.of(96, 56, 11, 12, 76, 30)),
  ISR(ProductMarketType.MRM, "ISRAEL", ImmutableSet.of(97, 19, 20, 57, 77, 30)),
  NA(ProductMarketType.MRM, "NORTH_AFRICA", ImmutableSet.of(16, 98, 58, 30, 78, 15)),
  NAR(ProductMarketType.NAR, "NORTH_AMERICA", ImmutableSet.of(3, 51, 4, 71, 91, 30)),
  SA(ProductMarketType.MRM, "SOUTH_AFRICA", ImmutableSet.of(17, 18, 99, 59, 30, 79)),
  SAM(ProductMarketType.MRM, "SOUTH_AMERICA", ImmutableSet.of(80, 100, 5, 6, 60, 30)),
  SAMPLES(ProductMarketType.EU, "EUROPE", ImmutableSet.of(2, 50, 14)),
  TUR(ProductMarketType.MRM, "TURKEY", ImmutableSet.of(81, 21, 101, 22, 61, 30)),
  WOM_AGCC(ProductMarketType.WOM, "WOM_AGCC", ImmutableSet.of(30)),
  WOM_ASIA(ProductMarketType.WOM, "WOM_ASIA", ImmutableSet.of(30)),
  WOM_AUS(ProductMarketType.WOM, "WOM_AUS", ImmutableSet.of(30)),
  WOM_CHILE(ProductMarketType.WOM, "WOM_CHILE", ImmutableSet.of(30)),
  WOM_EU(ProductMarketType.WOM, "WOM_EU", ImmutableSet.of(30)),
  WOM_INDIA(ProductMarketType.WOM, "WOM_INDIA", ImmutableSet.of(30)),
  WOM_ISR(ProductMarketType.WOM, "WOM_ISR", ImmutableSet.of(30)),
  WOM_NA(ProductMarketType.WOM, "WOM_NA", ImmutableSet.of(30)),
  WOM_NAR(ProductMarketType.WOM, "WOM_NAR", ImmutableSet.of(30)),
  WOM_SA(ProductMarketType.WOM, "WOM_SA", ImmutableSet.of(30)),
  WOM_SAM(ProductMarketType.WOM, "WOM_SAM", ImmutableSet.of(30)),
  WOM_TUR(ProductMarketType.WOM, "WOM_TUR", ImmutableSet.of(30));

  private final ProductMarketType productMarketType;
  private final String value;
  private final Collection<Integer> productIds;

  private ProductMarket(ProductMarketType productMarketType, String value, Collection<Integer> productIds)
  {
    this.productMarketType = productMarketType;
    this.value = value;
    this.productIds = productIds;
  }

  public ProductMarketType getProductMarketType()
  {
    return productMarketType;
  }

  public String getValue()
  {
    return value;
  }

  public Collection<Integer> getProductIds()
  {
    return productIds;
  }

  public boolean isMRM()
  {
    return ProductMarketType.MRM == this.productMarketType;
  }

  public boolean isWOM()
  {
    return ProductMarketType.WOM == this.productMarketType;
  }
}
