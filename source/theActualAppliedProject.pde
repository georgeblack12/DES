String message="1011100000011001011011110100101011000100101100001100111011010110";
String theKey= "00011011010000010111111111001110100111011010111010011010";
int k=0;
DES code;
void setup() {
  code=new DES(message,theKey);
}

void draw() {
  code.djKhaled();
  code.feistalFunction();
   noLoop();
}
