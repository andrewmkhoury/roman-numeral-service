# Core
This project implements an OSGi bundle implemting a simple converter to convert from an integer in the range 1-3999 to a Roman Numeral string.

# API
To convert an integer to a Roman Numeral, call [this static method](https://github.com/andrewmkhoury/roman-numeral-service/blob/e0229112f597bb29565d436b736b69cbccca9ebc/roman-numeral-service.core/src/main/java/com/akhoury/romannumeral/RomanNumeralConverter.java#L56):

  com.akhoury.romannumeral.RomanNumeralConverter.convertToRomanNumeral(int positiveInteger)
  
The implementation here is similar to methods in the `java.lang.Math` library.

# Unit Testing
There is [one unit test](https://github.com/andrewmkhoury/roman-numeral-service/blob/e0229112f597bb29565d436b736b69cbccca9ebc/roman-numeral-service.core/src/test/java/com/akhoury/romannumeral/RomanNumeralConverterTest.java#L42) that thoroughly tests the conversion using a broad range of example conversions from a text file [romannumeraltestdata.txt](https://github.com/andrewmkhoury/roman-numeral-service/blob/master/roman-numeral-service.core/src/test/resources/com/akhoury/romannumeral/romannumeraltestdata.txt)

