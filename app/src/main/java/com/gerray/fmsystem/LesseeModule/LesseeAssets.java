package com.gerray.fmsystem.LesseeModule;

public class LesseeAssets {
    private String assetName;
    private String assetModel;
    private String assetSerialNo;
    private String location;
    private String purchaseDate;
    private String addedDate;
    private String description;
    private String assetID;
    private String mImageUrl;

    public LesseeAssets() {
    }

    public LesseeAssets(String assetName, String assetModel, String assetSerialNo, String location, String purchaseDate, String addedDate, String description, String assetID, String mImageUrl) {
        this.assetName = assetName;
        this.assetModel = assetModel;
        this.assetSerialNo = assetSerialNo;
        this.location = location;
        this.purchaseDate = purchaseDate;
        this.addedDate = addedDate;
        this.description = description;
        this.assetID = assetID;
        this.mImageUrl = mImageUrl;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetModel() {
        return assetModel;
    }

    public void setAssetModel(String assetModel) {
        this.assetModel = assetModel;
    }

    public String getAssetSerialNo() {
        return assetSerialNo;
    }

    public void setAssetSerialNo(String assetSerialNo) {
        this.assetSerialNo = assetSerialNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
