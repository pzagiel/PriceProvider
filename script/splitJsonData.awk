awk 'BEGIN { 
RS="\n";
FS=":";
} 
{
filename="PriceData/"substr($2,2)".json";
print >filename;close(filename)
}' <"priceData/BloombergPrices.txt"

