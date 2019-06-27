package aws.fillspec;

/**
 * Product market types.
 * Note that RDW and MRM are the same.
 *
 * @author Mykola Pashkovskyi 
 * Date 04/01/2018.
 */
public enum ProductMarketType
{
  UNDEFINED("Undefined"),
  EU("Europe"),
  NAR("North America"),
  MRM("Rest of World"),
  WOM("World Overview Map");

  private final String value;

  ProductMarketType(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
