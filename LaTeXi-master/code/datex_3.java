public class PayloadPublication{
    Language defaultLanguage;
    MultilingualString feedDescription;
    String[] feedType;
    Date publicationTime;
    ElaboratedDataPublication elaboratedDataPublication;
    MeasuredDataPublication measuredDataPublication;
    SituationRecord situationRecord;
    ElaboratedData elaboratedData;

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }
    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
    public MultilingualString getFeedDescription() {
        return feedDescription;
    }
    public void setFeedDescription(MultilingualString feedDescription) {
        this.feedDescription = feedDescription;
    }
    public String[] getFeedType() {
        return feedType;
    }
    public void setFeedType(String[] feedType) {
        this.feedType = feedType;
    }
    public Date getPublicationTime() {
        return publicationTime;
    }
    public void setPublicationTime(Date publicationTime) {
        this.publicationTime = publicationTime;
    }
    public ElaboratedDataPublication getElaboratedDataPublication() {
        return elaboratedDataPublication;
    }
    public void setElaboratedDataPublication(ElaboratedDataPublication elaboratedDataPublication) {
        this.elaboratedDataPublication = elaboratedDataPublication;
    }
    public MeasuredDataPublication getMeasuredDataPublication() {
        return measuredDataPublication;
    }
    public void setMeasuredDataPublication(MeasuredDataPublication measuredDataPublication) {
        this.measuredDataPublication = measuredDataPublication;
    }
    public SituationRecord getSituationRecord() {
        return situationRecord;
    }
    public void setSituationRecord(SituationRecord situationRecord) {
        this.situationRecord = situationRecord;
    }
    public ElaboratedData getElaboratedData() {
        return elaboratedData;
    }
    public void setElaboratedData(ElaboratedData elaboratedData) {
        this.elaboratedData = elaboratedData;
    }
}