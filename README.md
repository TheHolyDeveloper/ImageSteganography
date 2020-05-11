# ImageSteganography
Image Steganography is a concept where data is hidden inside an image and decrypted at the other end . </br> </br>
ON running the application  , the GUI will provide  2 options ENCODE and DECODE </br> </br>
ENCODE -> On clicking this button User will be propmted to select an image , embed the  data  and then save it into a new file . </br>
DECODE -> User can select the encoded image and then decode the encrypted message inside an image .

Java program based on stegonographical methods to hide files in images using the Least Significant Bit technique.

I used the most basic method which is the least significant bit. A colour pixel is composed of red, green and blue, encoded on one byte. The idea is to store information in the first bit of every pixel's RGB component. In the worst case, the decimal value is different by one which is not visible to the human eye. In practice, if you don't have space to store all of your data in the first bit of every pixel you should start using the second bit, and so on.

<b>Information<b/>
LSBSteg is based on the concept of hiding hide data in images. It uses the first bit of every pixel, and every colour of an image. The code is quite simple to understand. If every first bit has been used, the module starts using the second bit, so the larger the data, the more the image is altered. The program can hide all of the data if there is enough space in the image. The main functions are:

embed_integer: The program hides the message count in the first 32 pixels using 1 bit per pixel .
encode_message: You provide an  image and a message  ,then  the method iterates for every pixel in order to hide them. A good practice is to have a carrier 8 times bigger than the image to hide (so that each pixel will be put only in the first bit).
Only images without compression are supported, namely not JPEG as LSB bits might get tampered during the compression phase.</br></br>

License
This software is MIT-Licensed.
