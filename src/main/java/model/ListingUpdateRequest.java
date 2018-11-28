package model;


import java.io.Serializable;

public class ListingUpdateRequest implements Serializable {

    //make the deserializer use set and
    private String sku;
    private String asin;
    private FulfillmentChannel fulfillmentChannel;

    //Inventory configurations
    private Double cost;
    private Double shippingCost;
    private Double additionalInventoryCosts;
    private String inventoryComment;
    private String vendorName;
    private String vendorPartNumber;
    private String brand;
    private Double leadTime;
    private Integer unitsInPack;
    private Integer minQuantityForOrder;
    private String parentSKU;
    private Double daysOfCoverage;
    private Boolean isReplenishable;
    private Long warehouseInventory;


    //Listing
    private Boolean repricerActivated;
    private Double floorPrice;
    private Double ceilingPrice;
    private RepricingMethod repricingMethod;
    private String repricingMethodValue;

    //Map Price
    private Float mapPrice;
    private Boolean mapPriceActivated;

    //repricer properties (kept for backward compatibility)
    private Integer aggressiveness;
    private Integer targetBbox;

    //item on sale fields
    private Long itemOnSaleStartDate;
    private Long itemOnSaleEndDate;
    private RepricingMethod itemOnSaleRepricingMethod;
    private String itemOnSaleRepricingMethodValue;
    private Double vat;
    private Double markup;
    private Double additionalSellingCosts;
    private String listingComment;
    private Double saleListedPrice;
    private BusinessPrice businessPrice;

    //Multi-Channel specific
    //listingConfiguration
    private Integer referenceAccountId;
    private String referenceSKU;
    private String referenceASIN;
    private FulfillmentChannel referenceFulfillmentChannel;
    private Double referencePriceChange;
    private Long feedvisorListingId;
    private AutomaticRepricingMethod automaticAutoRepricingMethod;
    private Double fixedPrice;

}
