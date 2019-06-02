package com.example.myiotdevice;

public class Sensors {

    public String itemHeader;
    public String itemText1;
    public String itemText2;
    public String itemText3;
    public String itemValue1;
    public String itemValue2;
    public String itemValue3;
    public String splittedItemHeader1;
    public String splittedItemHeader2;
    public String splittedItemValue1;
    public String splittedItemValue2;

    public Sensors(String itemHeader,String itemText1,String itemText2,String itemText3,String itemValue1,String itemValue2,String itemValue3,String splittedItemHeader1,String splittedItemHeader2,String splittedItemValue1,String splittedItemValue2){
        this.itemHeader=itemHeader;
        this.itemText1=itemText1;
        this.itemText2=itemText2;
        this.itemText3=itemText3;
        this.itemValue1=itemValue1;
        this.itemValue2=itemValue2;
        this.itemValue3=itemValue3;
        this.splittedItemHeader1=splittedItemHeader1;
        this.splittedItemHeader2= splittedItemHeader2;
        this.splittedItemValue1=splittedItemValue1;
        this.splittedItemValue2=splittedItemValue2;
    }
}
