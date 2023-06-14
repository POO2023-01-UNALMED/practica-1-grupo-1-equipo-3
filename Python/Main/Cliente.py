

class Cliente():
    def __init__(self, nombre, noDeIdentificacion):
        from Banco import Banco
        self.nombre = nombre
        self.Id = noDeIdentificacion
        self.tarjetasDebito = []
        self.tarjetasCredito = []
        self.facturas = []
        self.bonoActual = 0
        Banco.agregarCliente(self)
    
    def getNombre(self):
        return self.nombre