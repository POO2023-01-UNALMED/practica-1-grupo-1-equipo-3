from Transaccion import Transaccion
from TarjetaDebito import TarjetaDebito

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
    
    def revisarHistorialDeCreditos(self):
        from TarjetaCredito import TarjetaCredito
        retorno = []
        for t in Transaccion.getTransacciones():
            if t.getClienteOrigen() == self and not t.rechazado() and isinstance(t.getTarjetaOrigen(), TarjetaCredito) and not t.pendiente():
                retorno.append(t)
        return retorno
    
    def getTarjetasCredito(self):
        return self.tarjetasCredito
    def getTarjetasDebito(self):
        return self.tarjetasDebito
    
    def getFacturas(self):
        return self.facturas