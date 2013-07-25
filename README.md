HealthCareApp
=============

The Mobile Solution to maintain consumption of daily use edibles and calorie balance with interactive user interfaces, Chart and Nutrition labels calculated in such a way that are readable to user.

This app takes an image of  nutrition chart of the product which user wants to intake. OpenCV is used to clean the captured image. Then the image goes to OCR(Optical Character Recognation) which extract the information from the image and added them to user database that he has consumed it, the information  includes Calories, Total Fats and other Nutritions. 

If user want to gain or reduce weight, the formula in the main of the app helps user to input in how many days he want to reduce the weight and how much he wants to reduce. This will give him the Calories he wanted to take take everyday.
User can manually add products or exercise too. Products that he intake increase the calories and exercise that he do reduces the calories.

There is a chart that helps user to see his previouse performance regards calories as well.

I have forked [android-ocr](https://github.com/rmtheis/android-ocr/tree/master/android/src) with some changes for openCV for cleansing the image for my help. 
##Requires
[tess-two](https://github.com/rmtheis/tess-two/tree/master/tess-two)

[OpenCV for android](http://opencv.org/)


