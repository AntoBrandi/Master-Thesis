private double getDistanceFromPoint(double latB,double lonB){
    double latA = latitude;
    double lonA = longitude;
    double distance=0;

    // Conversion in Radianti
    latB = convertDegreesInRadiants(latB);
    lonB = convertDegreesInRadiants(lonB);
    latA = convertDegreesInRadiants(latA);
    lonA = convertDegreesInRadiants(lonA);

    distance = EARTH_RADIUS * Math.acos((Math.sin(latA)*Math.sin(latB)+
    Math.cos(latA)*Math.cos(latB)*Math.cos(lonA-lonB)));

    return distance;
}
private double convertDegreesInRadiants(double deg){
    double rad = deg *Math.PI / 180;
    return  rad;
}