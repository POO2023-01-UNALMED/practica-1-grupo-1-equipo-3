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
        Banco.agregarCliente(self)
    
    def getNombre(self):
        return self.nombre
    
    def revisarHistorialDeCreditos(self):
        from TarjetaCredito import TarjetaCredito
        retorno = []
        for t in Transaccion.getTransacciones():
            if t.getClienteOrigen() == self and not t.rechazado and isinstance(t.getTarjetaOrigen(), TarjetaCredito) and not t.pendiente:
                retorno.append(t)
        return retorno
    
    def getTarjetasCredito(self):
        return self.tarjetasCredito
    def getTarjetasDebito(self):
        return self.tarjetasDebito
    
    def getFacturas(self):
        return self.facturas
    
    def agregarFactura(self, f):
        self.facturas.append(f)

    def getTarjetas(self):
        return self.tarjetasCredito + self.tarjetasDebito
    
    def listarTarjetas(self, factura):
        tarjetasDisponibles = []
        for tarjeta in self.tarjetasDebito:
            if not tarjeta.tieneSaldo():
                continue
            if tarjeta.getDivisa() == factura.getDIVISA():
                tarjetasDisponibles.append(tarjeta)
        for tarjeta in self.tarjetasCredito:
            if not tarjeta.tieneSaldo():
                continue
            if tarjeta.getDivisa() == factura.getDIVISA():
                tarjetasDisponibles.append(tarjeta)
        return tarjetasDisponibles
    
    def agregarTarjetasDebito(self, *tarjetasDebito):
        self.tarjetasDebito.extend(list(tarjetasDebito))
    
    def agregarTarjetasCredito(self, *tarjetasCredito):
        self.tarjetasCredito.extend(list(tarjetasCredito))

    def encontrarTarjeta(self, tarjeta):
        for t in self.tarjetasCredito:
            if t.__str__() == tarjeta.__str__():
                return t
        for t in self.tarjetasDebito:
            if t.__str__() == tarjeta.__str__():
                return t
    
    def encontrarFacturas(self, factura):
        for f in self.facturas:
            if f.__str__() == factura.__str__():
                return f

    def mostrarFacturas(self):
        retorno = []
        for f in self.facturas:
            if not f.facturaPagada:
                retorno.append(f)
        return retorno