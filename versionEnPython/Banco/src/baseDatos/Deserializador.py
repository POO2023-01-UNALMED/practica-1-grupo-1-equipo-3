import pickle

class Deserializador():

    @staticmethod
    def DeserializarClientes():
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/clientes.pkl", "rb")
        datos = pickle.load(pickleFile)
        pickleFile.close()
        return datos[0]

    @staticmethod
    def DeserializarCanales():
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/canales.pkl", "rb")
        datos = pickle.load(pickleFile)
        pickleFile.close()

        return datos[0]

    @staticmethod
    def DeserializarTransacciones():
        pickleFile = open("versionEnPython/Banco/src/baseDatos/temp/transacciones.pkl", "rb")
        datos = pickle.load(pickleFile)
        pickleFile.close()
        return datos[0]
