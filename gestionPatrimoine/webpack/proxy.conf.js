function setupProxy() {
  const tls = process.env.TLS;
  const conf = [
    {
      context: [
        '/api',
        '/services',
        '/management',
        '/swagger-resources',
        '/v2/api-docs',
        '/v3/api-docs',
        '/h2-console',
        '/oauth2',
        '/login',
        '/auth',
        '/health',
      ],
      target: `http${tls ? 's' : ''}://10.198.100.208:9000`,
      secure: true,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy();
