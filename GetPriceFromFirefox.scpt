FasdUAS 1.101.10   ��   ��    k             l    ! ����  O     !  	  k      
 
     l   ��  ��    { uset targetURL to ("https://www.bloomberg.com/markets/api/bulk-time-series/price/BABA:US?timeFrame=1_MONTH") as string     �   � s e t   t a r g e t U R L   t o   ( " h t t p s : / / w w w . b l o o m b e r g . c o m / m a r k e t s / a p i / b u l k - t i m e - s e r i e s / p r i c e / B A B A : U S ? t i m e F r a m e = 1 _ M O N T H " )   a s   s t r i n g      l   ��  ��    l fopen location "https://www.bloomberg.com/markets/api/bulk-time-series/price/BABA:US?timeFrame=1_MONTH"     �   � o p e n   l o c a t i o n   " h t t p s : / / w w w . b l o o m b e r g . c o m / m a r k e t s / a p i / b u l k - t i m e - s e r i e s / p r i c e / B A B A : U S ? t i m e F r a m e = 1 _ M O N T H "      l   ��������  ��  ��        I   	�� ��
�� .GURLGURLnull��� ��� TEXT  m       �   � h t t p s : / / w w w . b l o o m b e r g . c o m / m a r k e t s / a p i / b u l k - t i m e - s e r i e s / p r i c e / B A B A : U S ? t i m e F r a m e = 1 _ M O N T H��        I  
 ������
�� .miscactvnull��� ��� null��  ��        l   ��   !��     ) #set theURL to URL of front document    ! � " " F s e t   t h e U R L   t o   U R L   o f   f r o n t   d o c u m e n t   # $ # r     % & % n     ' ( ' 1    ��
�� 
conT ( 4   �� )
�� 
docu ) m    ����  & o      ���� 0 	pricedata   $  * + * I   �� ,��
�� .JonspClpnull���     **** , o    ���� 0 	pricedata  ��   +  - . - l   �� / 0��   / H Bdo shell script ("echo " & (the clipboard) & " >> ~/Desktop/BABA")    0 � 1 1 � d o   s h e l l   s c r i p t   ( " e c h o   "   &   ( t h e   c l i p b o a r d )   &   "   > >   ~ / D e s k t o p / B A B A " ) .  2 3 2 l   ��������  ��  ��   3  4 5 4 l   ��������  ��  ��   5  6 7 6 l   ��������  ��  ��   7  8�� 8 l   ��������  ��  ��  ��   	 m      9 9�                                                                                  sfri  alis    N  Macintosh HD               �/�yH+     G
Safari.app                                                      ��Mӛf]        ����  	                Applications    �/�Y      ӛJ=       G  %Macintosh HD:Applications: Safari.app    
 S a f a r i . a p p    M a c i n t o s h   H D  Applications/Safari.app   / ��  ��  ��     : ; : l     ��������  ��  ��   ;  < = < l     ��������  ��  ��   =  >�� > i      ? @ ? I      �� A����  0 saveclipinfile SaveClipInFile A  B�� B o      ���� 0 ticker Ticker��  ��   @ k      C C  D E D I    �� F��
�� .sysoexecTEXT���     TEXT F l    	 G���� G b     	 H I H b      J K J m      L L � M M 
 e c h o   K l    N���� N I   ������
�� .JonsgClp****    ��� null��  ��  ��  ��   I m     O O � P P 6   > >   ~ / D e s k t o p / p r i c e D a t a . t x t��  ��  ��   E  Q�� Q I   �� R��
�� .sysodlogaskr        TEXT R m     S S � T T 
 H e l l o��  ��  ��       �� U V W��   U ������  0 saveclipinfile SaveClipInFile
�� .aevtoappnull  �   � **** V �� @���� X Y����  0 saveclipinfile SaveClipInFile�� �� Z��  Z  ���� 0 ticker Ticker��   X ���� 0 ticker Ticker Y  L�� O�� S��
�� .JonsgClp****    ��� null
�� .sysoexecTEXT���     TEXT
�� .sysodlogaskr        TEXT�� �*j %�%j O�j  W �� [���� \ ]��
�� .aevtoappnull  �   � **** [ k     ! ^ ^  ����  ��  ��   \   ]  9 ������������
�� .GURLGURLnull��� ��� TEXT
�� .miscactvnull��� ��� null
�� 
docu
�� 
conT�� 0 	pricedata  
�� .JonspClpnull���     ****�� "� �j O*j O*�k/�,E�O�j OPUascr  ��ޭ