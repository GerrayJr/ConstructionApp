package com.gerray.fmsystem;

public class FacilityAssets {
    private String AssetName;
    private String AssetModel;
    private String AssetSerialNo;
    private String Location;
    private String PurchaseDate;
    private String AddedDate;
    private String Description;
    private String AssetID;
    private String MImageUrl;

    public FacilityAssets() {
    }

    public FacilityAssets(String assetName, String assetModel, String assetSerialNo, String location, String purchaseDate, String addedDate, String description, String assetID, String mImageUrl) {
        AssetName = assetName;
        AssetModel = assetModel;
        AssetSerialNo = assetSerialNo;
        Location = location;
        PurchaseDate = purchaseDate;
        AddedDate = addedDate;
        Description = description;
        AssetID = assetID;
        MImageUrl = mImageUrl;
    }

    public String getAssetName() {
        return AssetName;
    }

    public void setAssetName(String assetName) {
        AssetName = assetName;
    }

    public String getAssetModel() {
        return AssetModel;
    }

    public void setAssetModel(String assetModel) {
        AssetModel = assetModel;
    }

    public String getAssetSerialNo() {
        return AssetSerialNo;
    }

    public void setAssetSerialNo(String assetSerialNo) {
        AssetSerialNo = assetSerialNo;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAssetID() {
        return AssetID;
    }

    public void setAssetID(String assetID) {
        AssetID = assetID;
    }

    public String getmImageUrl() {
        return MImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.MImageUrl = mImageUrl;
    }
}
