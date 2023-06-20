import pickle

class Serializador():

    @staticmethod
    def SerializarClientes(lista):
        datos = []
        datos.append(lista)
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/clientes.pkl", "wb")
        pickle.dump(datos, pickleFile)
        pickleFile.close()

    @staticmethod
    def SerializarCanales(lista):
        datos = []
        datos.append(lista)
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/canales.pkl", "wb")
        pickle.dump(datos, pickleFile)
        pickleFile.close()

    @staticmethod
    def SerializarTransacciones(lista):
        datos = []
        datos.append(lista)
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/transacciones.pkl", "wb")
        pickle.dump(datos, pickleFile)
        pickleFile.close()