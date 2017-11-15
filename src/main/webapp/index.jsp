<html>
<body>
<h1>API REST</h1>

<table>
    <tr>
        <th>url</th>
        <th>arguments</th>
        <th>type</th>
    </tr>
    <tr>
        <td>/login</td>
        <td>
            <ul>
                <li>login</li>
                <li>password</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>String</li>
                <li>String</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>/logout</td>
        <td>
            <ul>
                <li>login</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>String</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>/inscription</td>
        <td>
            <ul>
                <li>login</li>
                <li>password</li>
                <li>mail</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>String</li>
                <li>String</li>
                <li>String</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>/getWallet</td>
        <td>
            <ul>
                <li>personId</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>int</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>/pay</td>
        <td>
            <ul>
                <li>from</li>
                <li>to</li>
                <li>what</li>
                <li>value</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>int</li>
                <li>int</li>
                <li>Restricted String</li>
                <li>double</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>/showtrade</td>
        <td>
            <ul>
                <li>currency</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>CURRENCY</li>
            </ul>
        </td>
    </tr>
</table>

<h1>API PUBLIC</h1>
<table>
    <tr>
        <th>Entreprise</th>
        <th>url</th>
        <th>arguments</th>
        <th>type</th>
    </tr>
    <tr>
        <td>Poloniex</td>
        <td>/getChartData</td>
        <td>
            <ul>
                <li>pair_currency</li>
                <li>period</li>
                <li>start</li>
                <li>end</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>Restricted String</li>
                <li>Restricted String</li>
                <li>long</li>
                <li>long</li>
            </ul>
        </td>
    </tr>

    <tr>
        <td>Bloomsberg</td>
        <td>/requestNews</td>
        <td>
            <ul>
                <li>nbNews</li>
            </ul>
        </td>
        <td>
            <ul>
                <li>int</li>
            </ul>
        </td>
    </tr>
</table>

</body>

</html>
