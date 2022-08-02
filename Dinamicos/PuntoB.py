import numpy as np
"""
Nombres de los archivos de lectura y escritura, modificar para las distintas pruebas
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
        line += 1
        libros = []
        for i in range(m):
            data = readLine(line).split(" ")
            nombre = data[0]
            paginas = int(data[1])
            libros.append(Libro(nombre, paginas))
            line += 1
        return Entrada(n, m, libros)


def output(obj):
    '''
    Método para realizar la escritura de la respuesta del problema, no modificar.
    '''
    out = str(obj.tiempoTotal) + "\n"
    for i in range(len(obj.libroQueEmpieza)):
        out += obj.libroQueEmpieza[i] + " " + obj.libroQueTermina[i] + "\n"

    with open(nombreEscritura+".txt", "w") as f:
        f.write(out)


def solve(n, m, libros):
    
    inputs = np.zeros(n, int)
    outputs = np.zeros(n, int)
    matrix = np.full((n+1, m), np.Infinity)

    def mapMatrix(n, m, books):
        n = min(n, m)
        for i in range(1, m):
            books[i] += books[i - 1]
        for i in range(1, n + 1):
            for j in range(m):
                if i == 1 or j == 0:
                    matrix[i][j] = books[j]
                else:
                    for k in range(j-1, -1, -1):
                        num = max(matrix[i-1][k], books[j] - books[k])
                        matrix[i][j] = min(matrix[i][j], num)
        return matrix[n][m-1]

    def writers(n, m):
        outputs[n-1] = m
        while 0 < m:
            if matrix[n][m-1] < matrix[n-1][m-1]:
                if n == 2 and m == 3 and matrix[n][m-1] != matrix[n][m-2]:
                    outputs[n-2] = m - 1
                    inputs[n-1] = m
                    n = n - 1
                elif m == 2 and n == 2:
                    outputs[n-2] = m - 1
                    inputs[n-1] = m
                    n = n - 1
                m = m - 1
            else:
                outputs[n-2] = m - 1
                inputs[n-1] = m
                n = n - 1
                m = m - 1
        inputs[n-1] = 1 if inputs[n] != 1 else 0
    newBooks = [i.paginas for i in libros]
    result = int (mapMatrix(n, m, newBooks))
    inputs = inputs.astype(str)
    outputs = outputs.astype(str)
    writers(n, m)
    return Respuesta(result, inputs, outputs)


def main():
    e = input()
    res = solve(e.n, e.m, e.libros)
    output(res)


if __name__ == "__main__":
    main()
