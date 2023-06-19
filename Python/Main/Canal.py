from Divisa import Divisa


class Canal:
    def __init__(self, tipoCanal, impuesto, *fondos):
        """
        Constructor de la clase Canal. Recibe el tipo de canal, el impuesto asociado y una lista variable de fondos por divisa.
        Los fondos por divisa se establecen en un diccionario donde las claves son objetos de la clase Divisa y los valores son los montos.
        El canal se agrega automáticamente a la lista de canales en la clase Banco.
        """
        from Banco import Banco
        self.tipoCanal = tipoCanal
        self.impuesto = impuesto
        self.fondosPorDivisa = {}
        for i, divisa in enumerate(Divisa):
            if i < len(fondos):
                self.fondosPorDivisa[divisa] = fondos[i]
        Banco.agregarCanal(self)

    def setFondos(self, divisa, monto):
        """
        Establece los fondos disponibles para una divisa específica.
        """
        self.fondosPorDivisa[divisa] = monto

    def getFondos(self, divisa):
        """
        Retorna los fondos disponibles para una divisa específica.
        """
        return self.fondosPorDivisa.get(divisa)

    def getImpuesto(self):
        """
        Retorna el impuesto asociado al canal.
        """
        return self.impuesto

    def __str__(self):
        """
        Método especial que retorna una representación en cadena del objeto Canal.
        """
        from Banco import Banco
        return f"Canal: {self.tipoCanal} #{Banco.getCanales().index(self) + 1}\nTasa de impuestos: {self.impuesto}"

    def tieneDivisa(self, divisa):
        """
        Verifica si el canal tiene fondos para una divisa específica.
        Retorna True si no tiene fondos para la divisa, False en caso contrario.
        """
        return divisa not in self.fondosPorDivisa

    def tieneFondosDeDivisa(self, divisa):
        """
        Verifica si el canal tiene fondos disponibles para una divisa específica.
        Retorna True si no tiene fondos disponibles, False en caso contrario.
        """
        return not self.fondosPorDivisa.get(divisa) > 0.0

    def finalizarConversion(self, transaccion, montoInicial):
        """
        Finaliza una transacción de conversión de divisas.
        Actualiza los fondos de las tarjetas y del canal, y marca la transacción como no pendiente.
        Retorna la transacción actualizada.
        """
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
        """
        Método estático para seleccionar el canal adecuado para una transacción de acuerdo a la divisa y la operación (retirar o no).
        Retorna una lista de canales que cumplen con los requisitos.
        """
        from Banco import Banco
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
        """
        Finaliza una transacción de acuerdo a si es una operación de retirar o no retirar dinero.
        Actualiza los fondos de las tarjetas y del canal, y marca la transacción como no pendiente.
        Retorna la transacción actualizada.
        """
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
