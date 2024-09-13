.globl main

.data

img: .string "lenna.rgb"
imggray: .string "lennagray.gray"
buffer_rgb: .space 786432
buffer_gray: .space 262144
bufferSh: .space 262144
Sh: .byte 1, 0, -1, 2, 0, -2, 1, 0, -1

.text

main:

la a0, img #a0 recebe o nome da imagem
la a1, buffer_rgb #a1 recebe buffer address
jal read_rgb_image

mv a2, a0 #move para a2 o comprimento do buffer_rgb
la a0, buffer_rgb
la a1, buffer_gray
jal rgb_to_gray

la a0, imggray
la a1, buffer_gray
li a2, 262144
jal write_gray_image

la a0, buffer_gray
la a1, Sh
la a2, bufferSh
jal convolution

beq zero, zero, END

######################################################
# Funcao: read_rgb_image
# Descricao: Le um ficheiro no formato RGB para um array em memória.
# Argumentos:
# a0 - nome do ficheiro
# a1 - buffer address
# Retorna:
# a0 - comprimento do buffer
# a1 - buffer address
######################################################
read_rgb_image:

addi sp, sp, -4 	#Para guardar o valor do registo s0
sw s0, 0(sp)

li a7, 1024 		#System call para abrir o ficheiro
mv s0, a1
li a1, 0
ecall

li a7, 63 		#System call para ler o ficheiro
mv a1, s0
li a2, 786432 		#bytes para representar a imagem 5x5
ecall

li a7, 57 		#System call para fechar o ficheiro
ecall

lw s0, 0(sp)		#Devolver o valor original de s0
addi sp, sp, 4 

jr ra


######################################################
# Funcao: rgb_to_gray
# Descricao: Converte a imagem de RGB para GRAY
# Argumentos:
# a0 - buffer do rgb
# a1 - buffer do gray
# a2 - tamanho do buffer
# Retorna:
#
#
######################################################
rgb_to_gray:

addi a3, zero, 30
addi a4, zero, 59
addi a5, zero, 11
addi a6, zero, 100
li t5, 786432
add t5, t5, a0 	     #Fim do buffer rgb

CICLO:	
	lbu t0, 0(a0)	#Lê a cor Red
	lbu t1, 1(a0)	#Lê a cor Green
	lbu t2, 2(a0)	#Lê a cor Blue

	mul t3, t0, a3
	mul t4, t1, a4
	add t3, t3, t4
	mul t4, t2, a5

	add t3, t3, t4
	div t3, t3, a6

	sb t3, 0(a1)	#Guardar o byte calculado 0.30t0 + 0.59t1 + 0.11t2

	addi a0, a0, 3	#Avança no buffer rgb
	addi a1, a1, 1	#Avança no buffer gray

	bne a0, t5, CICLO
	
jr ra

######################################################
# Funcao: write_gray_image
# Descricao: Escreve um ficheiro no formato GRAY.
# Argumentos:
#a0 - buffer da imagem rgb
#a1 - buffer da imagem gray
#a2 - tamanho 
# Retorna:
#
#
######################################################
write_gray_image:

mv t1, a1	#Guarda em t1 o buffer_gray

li a7, 1024	#Abrir um ficheiro que nao existe
li a1, 1
ecall

li a7, 64	#Escrever no ficheiro
mv a1, t1
ecall

li a7, 57	#Fecha o ficheiro	
ecall 

jr ra

######################################################
# Funcao: convolution
# Descricao: Calcula a convolução de uma imagem com um operador Sobel
# Argumentos:
#a0 - buffer da imagem gray
#a1 - buffer do operador sobel
#a2 - buffer para a imagem filtrada
# Retorna:
#
#
######################################################
convolution:

#	Primeira linha, pixel canto superior esquerdo
lbu t0, 512(a0)
lb t1, 7(a1)
mul t2, t0, t1

add t3, zero, zero
add t3, t3, t2

lbu t0, 513(a0)
lb t1, 8(a1)
mul t2, t0, t1

add t3, t3, t2

lbu t0, 1(a0)
lb t1, 5(a1)
mul t2, t0, t1

add t3, t3, t2

sb t3, 0(a2)
#		--

addi a0, a0, 1	#Avanço no buffer da iamgem gray
addi a2, a2, 1	#Avanço no buffer da imagem filtrada

#	Restantes pixeis da primeira linha menos o ultimo

add t5, zero, zero	#Variavel que vai ser usada para contar os pixeis percorridos
addi t6, zero, 510

CICLOlinha1:
	lbu t0, -1(a0)	#Pixel antes do pixel central
	lb t1, 3(a1)
	mul t2, t0, t1
	
	add t3, zero, zero
	add t3, t3, t2
	
	lbu t0, 1(a0)	#Pixel depois do pixel central
	lb t1, 2(a1)
	mul t2, t0, t1
	add t3, t3, t2
	
	lbu t0, 511(a0)	#Pixel adjacente à esquerda do pixel central
	lb t1, 6(a1)
	mul t2, t0, t1
	add t3, t3, t2
	
	lbu t0, 512(a0)	#Pixel por baixo do pixel central
	lb t1, 7(a1)
	mul t2, t0, t1
	add t3, t3, t2
	
	lbu t0, 512(a0)	#Pixel adjacente à direita do pixel central
	lb t1, 6(a1)
	mul t2, t0, t1
	add t3, t3, t2
	
	sb t3, 0(a2)
	addi a0, a0, 1	#Avanço no buffer da iamgem gray
	addi a2, a2, 1	#Avanço no buffer da imagem filtrada
	addi t5, t5, 1
	
	bne t5, t6, CICLOlinha1	# 512 pixeis na linha menos o primeiro e o ultimo = 510 pixeis que tera de percorrer
	
#		--

#	Ultimo pixel da primeira linha

lbu t0, -1(a0)
lb t1, 3(a1)
mul t2, t0, t1

add t3, zero, zero
add t3, t3, t2

lbu t0, 512(a0)
lb t1, 7(a1)
mul t2, t0, t1

add t3, t3, t2

lbu t0, 511(a0)
lb t1, 6(a1)
mul t2, t0, t1

add t3, t3, t2

sb t3, 0(a2)

#		--

addi a0, a0, 1	#Avanço no buffer da iamgem gray
addi a2, a2, 1	#Avanço no buffer da imagem filtrada


#	Linhas do meio menos a primeira e ultima		for(Linhas do meio, primeiro pixel)
#									for(pixeis do meio, menos o primeiro e ultimo)
#										Ultimo pixel das linhas

add a4, zero, zero	#Contar o numero de linhas
CICLOpplm:	lbu t0, -512(a0)		#CICLOpplm = CICLO Primeiro pixel das linhas do meio
		lb t1, 1(a1)
		mul t2, t0, t1

		add t3, zero, zero
		add t3, t3, t2

		lbu t0, -511(a0)
		lb t1, 2(a1)
		mul t2, t0, t1

		add t3, t3, t2

		lbu t0, 1(a0)
		lb t1, 5(a1)
		mul t2, t0, t1

		add t3, t3, t2

		lbu t0, 512(a0)
		lb t1, 7(a1)
		mul t2, t0, t1

		add t3, t3, t2

		lbu t0, 513(a0)
		lb t1, 8(a1)
		mul t2, t0, t1

		add t3, t3, t2
		
		sb t3, 0(a2)

		addi a0, a0, 1	#Avanço no buffer da iamgem gray
		addi a2, a2, 1	#Avanço no buffer da imagem filtrada
		
		
		add t5, zero, zero	#Contar o numero de pixeis da linha
CICLOpmlm:	add t3, zero, zero	#Pixeis do meio das linhas do meio

		lbu t0, -513(a0)
		lb t1, 0(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -512(a0)
		lb t1, 1(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -511(a0)
		lb t1, 2(a1)
		mul t2, t0, t1
		add t3, t3, t2		
		
		lbu t0, -1(a0)
		lb t1, 3(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 1(a0)
		lb t1, 5(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 511(a0)
		lb t1, 6(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 512(a0)
		lb t1, 7(a1)
		mul t2, t0, t1
		add t3, t3, t2		

		lbu t0, 513(a0)
		lb t1, 8(a1)
		mul t2, t0, t1
		add t3, t3, t2	
		
		sb t3, 0(a2)
		
		addi a0, a0, 1	#Avanço no buffer da iamgem gray
		addi a2, a2, 1	#Avanço no buffer da imagem filtrada
		addi t5, t5, 1	#Numero do pixel da linha
		
		addi t6, t6, 510	#512 pixeis menos o primeiro pixel e o ultimo
		bne t5, t6, CICLOpmlm
		
		add t3, zero, zero	#Ultimo pixel das linhas do meio
		
		lbu t0, -513(a0)
		lb t1, 0(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -512(a0)
		lb t1, 1(a1)
		mul t2, t0, t1
		add t3, t3, t2

		lbu t0, -1(a0)
		lb t1, 3(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 511(a0)
		lb t1, 6(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 512(a0)
		lb t1, 7(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		sb t3, 0(a2)

		addi a0, a0, 1	#Avanço no buffer da iamgem gray
		addi a2, a2, 1	#Avanço no buffer da imagem filtrada
		addi a7, a7, 1	#Incrementa a linha
		
		addi a6, zero, 510
		bne a4, a6, CICLOpplm
		
#		--

#		Ultima linha, primeiro pixel
		
		add t3, zero, zero
		lbu t0, -512(a0)
		lb t1, 1(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -511(a0)
		lb t1, 2(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 1(a0)
		lb t1, 5(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		sb t3, 0(a2)
		
		addi a0, a0, 1	#Avanço no buffer da iamgem gray
		addi a2, a2, 1	#Avanço no buffer da imagem filtrada
		
#		--

		add a7, zero, zero	#contador dos pixeis
CICLOpmlf:		#Pixeis do meio linha final	
		add t3, zero, zero
		lbu t0, -1(a0)
		lb t1, 3(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -513(a0)
		lb t1, 0(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -512(a0)
		lb t1, 1(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -511(a0)
		lb t1, 2(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, 1(a0)
		lb t1, 5(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		sb t3, 0(a2)
		
		addi a0, a0, 1	#Avanço no buffer da iamgem gray
		addi a2, a2, 1	#Avanço no buffer da imagem filtrada
		addi a7, a7, 1	#Avança no contador de pixeis
		
		addi a6, zero, 510
		bne a7,	a6, CICLOpmlf
#		--

#		Ultimo pixel da ultima linha
		
		add t3, zero, zero
		lbu t0, -1(a0)
		lb t1, 3(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -513(a0)
		lb t1, 0(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		lbu t0, -512(a0)
		lb t1, 1(a1)
		mul t2, t0, t1
		add t3, t3, t2
		
		sb t3, 0(a2)
#		--

jr ra
		
		
END:	
