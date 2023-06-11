from entidades_de_negocio.Divisa import Divisa
from infraestructura.Banco import Banco

class Canal:
    def _init_(self, tipoCanal, impuesto, *fondos):
        self.tipoCanal = tipoCanal
        self.impuesto = impuesto
        self.fondosPorDivisa = {}

        for i, divisa in enumerate(Divisa):
            if i < len(fondos):
                self.fondosPorDivisa[divisa] = fondos[i]

    def setFondos(self, divisa, monto):
        self.fondosPorDivisa[divisa] = monto

    def getFondos(self, divisa):
        return self.fondosPorDivisa.get(divisa)

    def getImpuesto(self):
        return self.impuesto

    def _str_(self):
        return f"Canal: {self.tipoCanal}\nTasa de impuestos: {self.impuesto}"

    def tieneDivisa(self, divisa):
        return divisa not in self.fondosPorDivisa

    def tieneFondosDeDivisa(self, divisa):
        return self.fondosPorDivisa.get(divisa, 0.0) > 0.0

    def finalizarConversion(self, transaccion, montoInicial):
        if transaccion.rechazado:
            transaccion.tarjetaOrigen.anadirTransaccionRechazada()

        if not transaccion.pendiente:
            return None

        divisaOrigen = transaccion.divisa
        fondosOrigen = self.getFondos(divisaOrigen)

        divisaDestino = transaccion.tarjetaObjetivo.divisa
        fondosDestino = self.getFondos(divisaDestino)

        transaccion.tarjetaOrigen.sacarDinero(montoInicial)
        self.setFondos(divisaOrigen, fondosOrigen + montoInicial)
        self.setFondos(divisaDestino, fondosDestino - transaccion.cantidad)
        transaccion.tarjetaObjetivo.introducirDinero(transaccion.cantidad)
        transaccion.pendiente = False

        return transaccion

    @staticmethod
    def seleccionarCanal(divisa, retirar):
        retorno = []
        if retirar:
            for canal in Banco.getCanales():
                if not canal.tieneDivisa(divisa):
                    retorno.append(canal)
        else:
            for canal in Banco.getCanales():
                if not canal.tieneDivisa(divisa):
                    if canal.tipoCanal == "Cajero":
                        continue
                    if not canal.tieneFondosDeDivisa(divisa):
                        retorno.append(canal)
        return retorno

    @staticmethod
    def finalizarTransaccion(transaccion, retirar):
        if transaccion.rechazado:
            transaccion.tarjetaOrigen.anadirTransaccionRechazada()

        if not transaccion.pendiente:
            return None

        if retirar:
            transaccion.tarjetaOrigen.sacarDinero(transaccion.cantidad)
            transaccion.canal.setFondos(transaccion.divisa, transaccion.canal.getFondos(transaccion.divisa) - transaccion.cantidad)
        else:
            transaccion.tarjetaObjetivo.introducirDinero(transaccion.cantidad)
            transaccion.canal.setFondos(transaccion.divisa, transaccion.canal.getFondos(transaccion.divisa) + transaccion.cantidad)
        transaccion.pendiente = False

        return transaccion