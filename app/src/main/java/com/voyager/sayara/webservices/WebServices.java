package com.voyager.sayara.webservices;

import android.support.annotation.Nullable;

import com.voyager.sayara.DriverTripDetailPage.model.DTDModel;
import com.voyager.sayara.MapPlaceSearch.model.placedetail.PlaceDetail;
import com.voyager.sayara.MapPlaceSearch.model.placesearch.PlacesResults;
import com.voyager.sayara.RetorHelper.OfferList;
import com.voyager.sayara.fare.model.TripResponse;
import com.voyager.sayara.landingpage.model.TripCarDetails;
import com.voyager.sayara.landingpage.model.geogetpath.GetPaths;
import com.voyager.sayara.landingpage.model.landingModel.EndTrip;
import com.voyager.sayara.registerpage.model.UserDetails;
import com.voyager.sayara.triphistroty.model.TripDetails;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebServices {
    //@GET("driver/getDriver/")
   // Call<MainClass> doGetUserList(@Query("page") String page);
    //http://10.1.1.18/sayara/user/booking/--pickup_loc: 9.731235,76.355463 -- user_id 89

    @GET("webservice/getOffertypes")
    Call<OfferList> doGetUserList();

    @FormUrlEncoded
    @POST("register/")
    public Call<UserDetails> registerUser(@Nullable @Field("name") String name,
                                          @Nullable @Field("password") String password,
                                          @Nullable @Field("email") String email,
                                          @Nullable @Field("phone") String phone,
                                          @Nullable @Field("country") String country,
                                          @Nullable @Field("city") String city);
    @FormUrlEncoded
    @POST("login/")
    public Call<UserDetails> loginUser(@Nullable @Field("email") String email,
                                     @Nullable @Field("password") String passwd,
                                       @Nullable @Field("token") String fireBaseToken);

    @FormUrlEncoded
    @POST("updateProfile/")
    Call<UserDetails> updateProfile(@Nullable @Field("name") String name,
                                    @Nullable @Field("userID") int id,
                                    @Nullable @Field("password") String password,
                                    @Nullable @Field("phone") String phone,
                                    @Nullable @Field("country") String country,
                                    @Nullable @Field("city") String city);
    @FormUrlEncoded
    @POST("updateProfile/")
    Call<UserDetails> updateProfilePass(@Nullable @Field("password") String name,
                                    @Nullable @Field("user_id") int id);
    @FormUrlEncoded
    @POST("updateProfile/")
    Call<UserDetails> updateProfileName(@Nullable @Field("name") String name,
                                        @Nullable @Field("user_id") int id,
                                        @Nullable @Field("password") String password,
                                        @Nullable @Field("city") String city);
    @FormUrlEncoded
    @POST("updateProfile/")
    Call<UserDetails> updateProfilePhno(@Nullable @Field("phone") String name,
                                    @Nullable @Field("user_id") int id);
    @FormUrlEncoded
    @POST("updateProfile/")
    Call<UserDetails> updateProfileCity(@Nullable @Field("city") String name,
                                    @Nullable @Field("user_id") int id);
    @FormUrlEncoded
    @POST("tripHistory/")
    Call<List<TripDetails>> getTripHistory(@Field("user_id") int userId,
                                           @Nullable @Field("page") int page);

    @FormUrlEncoded
    @POST("driverProfile/")
    Call<DTDModel> getDriverProfileDetail(@Nullable @Field("driver_id") int userId,
                                                     @Nullable @Field("user_id") int tripId);
    @FormUrlEncoded
    @POST("cancelTrip/")
    Call<EndTrip> stopStartUpTrip(@Nullable @Field("user_id") int userId,
                                  @Nullable @Field("trip_id") int tripId);
    @FormUrlEncoded
    @POST("cancelTrip/")
    Call<EndTrip> endOnGoingTrip(@Nullable @Field("user_id") int userId,
                                  @Nullable @Field("trip_id") int tripId);

    @FormUrlEncoded
    @POST("getCars/")
    Call<TripCarDetails> getTripDetails(@Nullable @Field("pickup_loc") String latLng,
                                        @Nullable @Field("user_id") int userId);
    @POST("FCMUpdateServlet")
    public Call<UserDetails> updateFCMId(@Nullable @Field("user_id") int userId,
                                         @Nullable @Field("token") String token);
    @FormUrlEncoded
    @POST("confirmTrip/")
    public Call<TripResponse> startTrip(@Nullable @Field("user_id") int userId,
                                        @Nullable @Field("user_name") String userName,
                                        @Nullable @Field("pickup_loc") String nameStartLoc,
                                        @Nullable @Field("pickup_address") String nameStart,
                                        @Nullable @Field("drop_loc") String nameEndLoc,
                                        @Nullable @Field("drop_address") String nameEnd,
                                        @Nullable @Field("distance") String distanceKm,
                                        @Nullable @Field("amount") String costFairSet,
                                        @Nullable @Field("car_id") String driveCarId,
                                        @Nullable @Field("pay_type") String paymentType);

    @Multipart
    @POST("/updateProfile/")
    public Call<UserDetails> uploadProfileImg(@Part MultipartBody.Part  profileImg, @Part("user_id") RequestBody userID);

    @GET("place/autocomplete/json?")
    Call<PlacesResults> getPlaceSearch(@Query("input") String input, @Query("types") String types, @Query("key") String key);
    //https://developers.google.com/maps/documentation/directions/intro
    @GET("directions/json?")
    Call<GetPaths> getTripRoute(@Query("origin") String source, @Query("destination") String destination, @Query("key") String key);
    @GET("place/details/json?")
    Call<PlaceDetail> getPlaceDetail(@Query("placeid") String source, @Query("key") String key);
    @GET("directions/json?")
    public Call<GetPaths> getPaths(@Query("origin") String origin, @Query("destination") String dest, @Query("sensor") Boolean sensor,@Query("key") String key);


    /* @Multipart
    @POST("DriverRegisterServlet")
    Call<UserModel> uploadFile(@Part MultipartBody.Part licenseFile, @Part MultipartBody.Part rcFile, @Part MultipartBody.Part profileFile, @Part("name") RequestBody name);
    @Multipart
    @POST("DriverProfileUpdateServlet")
    Call<UserModel> driverProfileUpdate(@Part MultipartBody.Part licenseFile, @Part MultipartBody.Part rcFile, @Part MultipartBody.Part profileFile, @Part("name") RequestBody name);

    @POST("AndroidLoginServlet")
    //@FormUrlEncoded
    public Call<UserModel> loginUser(@Body UserModel userModel);

    @POST("AndroidRegisterServlet")
    public Call<UserModel> registerUser(@Body UserModel userModel);

    @POST("AndroidUpdateProfileServlet")
    public Call<UserModel> updateProfile(@Body UserModel userModel);

    @POST("TripServlet")
    public Call<TripDetailsModel> userRequestTrip(@Body TripDetailsModel tripModel);

    @GET("HistoryServlet")
    public Call<List<TripDetailsModel>> getUserHistory(@Query("user_name") String email);

    @GET("DriverHistoryServlet")
    public Call<List<TripDetailsModel>> getDriverHistory(@Query("user_name") String email);

    @GET("AndroidGetProfileServlet")
    public Call<UserModel> getUserInfo(@Query("user_name") String email);

    @GET("DriverAcceptRejectServlet")
    public Call<UserModel> acceptRejectTrip(@Query("id") String id, @Query("status") String status);

    @POST("FCMUpdateServlet")
    public Call<UserModel> updateFCMId(@Body UserModel userModel);


    @GET("json")
    public Call<GetPaths> getPaths(@Query("origin") String origin, @Query("destination") String dest, @Query("sensor") String sensor);

    @POST("RechargeServlet")
    Call<RechargeModel> userWalletRecharge(@Body RechargeModel rechargeModel);

    @POST("LocationUpdateServlet")
    Call<UserModel> driverLocationUpdate(@Body UserModel userModel);

    @GET("GetPriceInfoServlet")
    Call<AutoChargeModel> updateFee();

    @GET("DriverProfileServlet")
    Call<UserModel> getDriverInfo(@Query("user_name") String email);

    @GET("StartStopTripServlet")
    Call<UserModel> startStopTrip(@Query("id") String id, @Query("status") String status, @Query("payment_mode") String paymentMode);

    @GET("UserFeedbackServlet")
    Call<UserModel> userFeedBack(@Query("id") String id, @Query("rating") String userRating);
    @GET("DriverLocationServlet")
    Call<UserModel> getDriverLocation(@Query("user_name") String username);*/
}
