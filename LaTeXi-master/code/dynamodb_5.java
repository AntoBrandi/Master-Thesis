@Override
protected String doInBackground(String... params) {
    try {
        PublicationDO publication = new PublicationDO();
        publication.where(latitude>=latA);    
        publication.where(latitude<=latB); 
        publication.where(longitude>=lonA);    
        publication.where(longitude<=lonB); 

        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue().withS("it"));
        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(publication)
                //.withRangeKeyCondition("creator", rangeKeyCondition)
                .withConsistentRead(false);

        result = dynamoDBMapper.query(PublicationDO.class, queryExpression);
    }
    catch (Exception e){}
    return "Executed";
}
