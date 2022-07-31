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
    matrix = np.full((n+1, m+1), np.Infinity)

    def mapMatrix(n, m, sum, maxsum):
        if m == 0:
            matrix[n][0] = maxsum if maxsum < matrix[n][0] else matrix[n][0]
            if n == 1:
                matrix[0][0] = matrix[n][0]
            return maxsum

        if n == 0:
            matrix[0][m] = np.Infinity
            return np.Infinity

        sum = sum + libros[m-1].paginas
        result = min(mapMatrix(n-1, m, 0, maxsum),
                     (mapMatrix(n, m-1, sum, sum if sum > maxsum else maxsum)))
        matrix[n][m] = result if result < matrix[n][m] else matrix[n][m]

        return result

    def writers(n, m):
        while 0 < m:
            if m + 1 == matrix[0].size:
                outputs[n-1] = m

            if matrix[n-1][m] < matrix[n][m-1]:
                inputs[n-1] = m + 1
                outputs[n-2] = m
                n = n - 1

            else:
                m = m - 1

        inputs[n-1] = 1
    inputs = inputs.astype(str)
    outputs = outputs.astype(str)
    result = mapMatrix(n, m, 0, 0)
    writers(n, m)
    return Respuesta(result, inputs, outputs)


def main():
    e = input()
    res = solve(e.n, e.m, e.libros)
    output(res)


if __name__ == "__main__":
    main()
