from unittest import result
import numpy as np
"""
Nombres de los archivos de lectura y escritura, modifique como considere.
 """
nombreLectura = "inB"
nombreEscritura = "outB"

class Entrada():
    def __init__(self, n, m, libros):
        self.n = n
        self.m = m
        self.libros = libros

class Respuesta():
    def __init__(self, tiempoTotal, libroQueEmpieza, libroQueTermina):
        self.tiempoTotal = tiempoTotal
        self.libroQueEmpieza = libroQueEmpieza
        self.libroQueTermina = libroQueTermina

class Libro():
    def __init__(self, nombre, paginas):
        self.nombre = nombre
        self.paginas = paginas

def input():
    '''
    Método para realizar la lectura del problema, no modificar.
    '''
    readData = []
    line = 0
    def readLine(line):
        inputLine = readData[line]
        return inputLine

    with open(nombreLectura+".txt", "r") as f:
        readData = f.read().split('\n')
        data = readLine(line).split(" ")
        n = int(data[0])
        m = int(data[1])
        line+=1
        libros = []
        for i in range(m):
            data = readLine(line).split(" ")
            nombre = data[0]
            paginas = int(data[1])
            libros.append(Libro(nombre, paginas))
            line+=1
        return Entrada(n, m, libros)



def output(obj):
    '''
    Método para realizar la escritura de la respuesta del problema, no modificar.
    '''
    out = str(obj.tiempoTotal) + "\n"
    for i in range(len(obj.libroQueEmpieza)):
        out+= obj.libroQueEmpieza[i] +" " +obj.libroQueTermina[i] +"\n"
    

    with open(nombreEscritura+".txt", "w") as f:
        f.write(out)





def solve(n, m, libros):
    
    inputs = np.zeros(n, int)
    outputs = np.zeros(n, int)

    def check(n, m, mid, books):
        count = 0
        sum = 0
        for i in range(m):
            sum += books[i]
            if (sum > mid):
                count += 1
                sum = books[i]

        count += 1
        if (count <= n):
            return True
        return False

    def checkbooksays(n, m, mid, books):
        count = 0
        sum = 0
        inputs[0] = 1
        for i in range(m):
            if (books[i] > mid):
                return False
            sum += books[i]
            if (sum > mid):
                outputs[count] = i
                count += 1
                inputs[count] = i + 1
                sum = books[i]
        outputs[count] = m
        count += 1
        if (count <= n):
            return True
        return False

    def makeResult(n, m, books):
        init = max(books)
        final = np.sum(books)
        result = 0
        while (init <= final):
            mid = (init + final) // 2
            if (check(n, m, mid, books)):
                result = mid
                final = mid - 1
            else:
                init = mid + 1
        checkbooksays(n, m, result, books)
        return result
    
    newBooks = [i.paginas for i in libros]
    result = makeResult(n, m, newBooks)
    inputs = inputs.astype(str)
    outputs = outputs.astype(str)
    return Respuesta(result, inputs, outputs)


def main():
    e = input()
    res = solve(e.n, e.m, e.libros)
    output(res)


if __name__ == "__main__":
    main()

