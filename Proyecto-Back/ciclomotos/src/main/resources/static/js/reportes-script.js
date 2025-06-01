let ventas = [];

async function calcularTotalesEnCurso() {
    try {
        const response = await fetch('api/ventas/obtenerVentas');
        if (!response.ok) throw new Error('Error al obtener las ventas');

        ventas = await response.json();
        console.log("Ventas recibidas:", ventas);

        const ahora = new Date();
        const inicioAño = new Date(ahora.getFullYear(), 0, 1);
        const inicioMes = new Date(ahora.getFullYear(), ahora.getMonth(), 1);

        const dia = ahora.getDay();
        const diferenciaLunes = dia === 0 ? -6 : 1 - dia;
        const inicioSemana = new Date(ahora);
        inicioSemana.setDate(ahora.getDate() + diferenciaLunes);
        inicioSemana.setHours(0, 0, 0, 0);

        let totalAño = 0;
        let totalMes = 0;
        let totalSemana = 0;

        ventas.forEach(v => {
            const fechaVenta = new Date(v.fecha);
            const total = parseFloat(v.total);

            if (fechaVenta >= inicioAño && fechaVenta <= ahora) {
                totalAño += total;
            }
            if (fechaVenta >= inicioMes && fechaVenta <= ahora) {
                totalMes += total;
            }
            if (fechaVenta >= inicioSemana && fechaVenta <= ahora) {
                totalSemana += total;
            }
        });

        /*
        const campoSemana = document.getElementById("ingresoVentas");
        const campoMes = document.getElementById("ingresoMes");
        const campoAnio = document.getElementById("ingresoAnio");

        if (campoSemana) campoSemana.value = totalSemana.toFixed(2);
        if (campoMes) campoMes.value = totalMes.toFixed(2);
        if (campoAnio) campoAnio.value = totalAño.toFixed(2);
        */
        document.getElementById("ingresoVentasAnual").innerText = totalSemana.toFixed(2);
        document.getElementById("ingresoVentasMes").innerText = totalSemana.toFixed(2);
        document.getElementById("ingresoVentasSemana").innerText = totalSemana.toFixed(2);

        mostrarTablaVentas(ventas);
    } catch (error) {
        console.error("Error al calcular totales:", error);
    }
}

document.addEventListener("DOMContentLoaded", calcularTotalesEnCurso);
function cargarGrafico() {
    fetch('api/ventas/obtenerVentas')
        .then(response => response.json())
        .then(ventas => {
            const ahora = new Date();

            const dia = ahora.getDay();
            const diferenciaLunes = dia === 0 ? -6 : 1 - dia;
            const inicioSemana = new Date(ahora);
            inicioSemana.setDate(ahora.getDate() + diferenciaLunes);
            inicioSemana.setHours(0, 0, 0, 0);

            const diasSemana = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];
            const totalesPorDia = Array(7).fill(0);

            ventas.forEach(v => {
                const fechaVenta = new Date(v.fecha);
                if (fechaVenta >= inicioSemana && fechaVenta <= ahora) {
                    let diaSemana = fechaVenta.getDay();
                    if (diaSemana === 0) diaSemana = 7;
                    totalesPorDia[diaSemana - 1] += v.total;
                }
            });

            // Crear gráfico
            const ctx = document.getElementById('graficoVentas').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: diasSemana,
                    datasets: [{
                        label: 'Ventas Semanales ($)',
                        data: totalesPorDia,
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        });
}
document.addEventListener("DOMContentLoaded", cargarGrafico);
function mostrarTablaVentas() {
    const tbody = document.getElementById('tablaVentasBody');
    tbody.innerHTML = '';

    ventas.forEach(venta => {
        const fecha = new Date(venta.fecha).toLocaleString();
        const cliente = venta.cliente.nombre;

        venta.detalles.forEach(detalle => {
            const fila = document.createElement('tr');

            fila.innerHTML = `
        <td>${fecha}</td>
        <td>${cliente}</td>
        <td>${detalle.nombreProducto}</td>
        <td>$${detalle.precioUnitario.toFixed(2)}</td>
        <td>${detalle.cantidad}</td>
        <td>$${venta.total.toFixed(2)}</td>
      `;

            tbody.appendChild(fila);
        });
    });
}


