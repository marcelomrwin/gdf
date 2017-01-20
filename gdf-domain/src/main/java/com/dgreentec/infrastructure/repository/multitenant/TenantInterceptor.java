package com.dgreentec.infrastructure.repository.multitenant;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.dgreentec.domain.model.Tenant;

public class TenantInterceptor {

	@AroundInvoke
	public Object wrapWithTenant(InvocationContext ctx) throws Exception {

		String tenantName = "comum";
		Object[] parameters = ctx.getParameters();
		if (parameters != null) {
			Object param = parameters[0];
			if (param instanceof Tenant) {
				Tenant t = (Tenant) param;
				tenantName = t.getSchemaName();
			}
		}

		final String oldValue = TenantHolder.getCurrentTenant();
		try {
			TenantHolder.setTenant(tenantName);

			return ctx.proceed();
		} finally {
			if (oldValue != null) {
				TenantHolder.setTenant(oldValue);
			} else {
				TenantHolder.cleanupTenant();
			}

		}

	}
}
