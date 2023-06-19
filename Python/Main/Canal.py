from Divisa import Divisa

class Canal:
    def __init__(self, tipoCanal, impuesto, *fondos):
        from Banco import Banco
        self.tipoCanal = tipoCanal
        self.impuesto = impuesto
        self.fondosPorDivisa = {}
        for i, divisa in enumerate(Divisa):
            if i < len(fondos):
                self.fondosPorDivisa[divisa] = fondos[i]
        Banco.agregarCanal(self)

    def setFondos(self, divisa, monto):
        self.fondosPorDivisa[divisa] = monto

    def getFondos(self, divisa):
        return self.fondosPorDivisa.get(divisa)

    def getImpuesto(self):
        return self.impuesto

    def __str__(self):
        from Banco import Banco
        return f"Canal: {self.tipoCanal} #{Banco.getCanales().index(self) + 1}\nTasa de impuestos: {self.impuesto}"

    def tieneDivisa(self, divisa):
        return divisa not in self.fondosPorDivisa

    def tieneFondosDeDivisa(self, divisa):
        return not self.fondosPorDivisa.get(divisa) > 0.0

    def finalizarConversion(self, transaccion, montoInicial):
        if transaccion.rechazado:
            transaccion.tarjeta_origen.anadirTransaccionRechazada()
        if not transaccion.pendiente:
            return None
        divisaOrigen = transaccion.divisa
        fondosOrigen = self.getFondos(divisaOrigen)

        divisaDestino = transaccion.tarjeta_objetivo.divisa
        fondosDestino = self.getFondos(divisaDestino)

        transaccion.tarjeta_origen.sacarDinero(montoInicial)
        self.setFondos(divisaOrigen, fondosOrigen + montoInicial)
        self.setFondos(divisaDestino, fondosDestino - transaccion.cantidad)
        transaccion.tarjeta_objetivo.introducirDinero(transaccion.cantidad)
        transaccion.pendiente = False

        return transaccion

    @staticmethod
    def seleccionarCanal(divisa, retirar):
        from Banco import Banco # Es necesario importar aquí, de otra manera, podría causar un error por import circular
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
            transaccion.tarjeta_origen.anadirTransaccionRechazada()

        if not transaccion.pendiente:
            return None

        if retirar:
            transaccion.tarjeta_origen.sacarDinero(transaccion.cantidad)
            transaccion.canal.setFondos(transaccion.divisa, transaccion.canal.getFondos(transaccion.divisa) - transaccion.cantidad)
        else:
            transaccion.tarjeta_objetivo.introducirDinero(transaccion.cantidad)
            transaccion.canal.setFondos(transaccion.divisa, transaccion.canal.getFondos(transaccion.divisa) + transaccion.cantidad)
        transaccion.pendiente = False

        return transaccion