<!doctype html>
<#include "../include/imports.ftl">
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <meta name="description" content="Example React SPA for Bloomreach Experience"/>
    <title>brXM + React = ♥️</title>
    <link rel="shortcut icon" type="image/png" href="http://localhost:3000/static/favicon.png" sizes="64x64"/>
    <link rel="stylesheet" href="/site/static/css/nhsuk-3.0.4.min.css" media="screen"/>
</head>
<body>
<noscript>You need to enable JavaScript to run this app.</noscript>
<div id="root"></div>

<script>!function (c) {
        function e(e) {
            for (var r, t, n = e[0], o = e[1], l = e[2], u = 0, a = []; u < n.length; u++) t = n[u], Object.prototype.hasOwnProperty.call(f, t) && f[t] && a.push(f[t][0]), f[t] = 0;
            for (r in o) Object.prototype.hasOwnProperty.call(o, r) && (c[r] = o[r]);
            for (s && s(e); a.length;) a.shift()();
            return i.push.apply(i, l || []), p()
        }

        function p() {
            for (var e, r = 0; r < i.length; r++) {
                for (var t = i[r], n = !0, o = 1; o < t.length; o++) {
                    var l = t[o];
                    0 !== f[l] && (n = !1)
                }
                n && (i.splice(r--, 1), e = u(u.s = t[0]))
            }
            return e
        }

        var t = {}, f = {1: 0}, i = [];

        function u(e) {
            if (t[e]) return t[e].exports;
            var r = t[e] = {i: e, l: !1, exports: {}};
            return c[e].call(r.exports, r, r.exports, u), r.l = !0, r.exports
        }

        u.m = c, u.c = t, u.d = function (e, r, t) {
            u.o(e, r) || Object.defineProperty(e, r, {enumerable: !0, get: t})
        }, u.r = function (e) {
            "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(e, "__esModule", {value: !0})
        }, u.t = function (r, e) {
            if (1 & e && (r = u(r)), 8 & e) return r;
            if (4 & e && "object" == typeof r && r && r.__esModule) return r;
            var t = Object.create(null);
            if (u.r(t), Object.defineProperty(t, "default", {
                enumerable: !0,
                value: r
            }), 2 & e && "string" != typeof r) for (var n in r) u.d(t, n, function (e) {
                return r[e]
            }.bind(null, n));
            return t
        }, u.n = function (e) {
            var r = e && e.__esModule ? function () {
                return e.default
            } : function () {
                return e
            };
            return u.d(r, "a", r), r
        }, u.o = function (e, r) {
            return Object.prototype.hasOwnProperty.call(e, r)
        }, u.p = "http://localhost:3000/";
        var r = this["webpackJsonp@bloomreach/example-react-csr"] = this["webpackJsonp@bloomreach/example-react-csr"] || [],
            n = r.push.bind(r);
        r.push = e, r = r.slice();
        for (var o = 0; o < r.length; o++) e(r[o]);
        var s = n;
        p()
    }([])</script>
<script src="/site/static/js/2.306dfc79.chunk.js"></script>
<script src="/site/static/js/main.6cdedca1.chunk.js"></script>
</body>
</html>