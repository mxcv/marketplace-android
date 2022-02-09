package com.company.marketplace.repositories;

import androidx.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JsonObjectRequestWithNull extends JsonRequest<JSONObject> {

	public JsonObjectRequestWithNull(int method, String url, JSONObject jsonRequest,
									 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(@NonNull NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
				HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)
			);
			return Response.success(
				jsonString.length() == 0 ? null : new JSONObject(jsonString),
				HttpHeaderParser.parseCacheHeaders(response)
			);
		} catch (UnsupportedEncodingException | JSONException e) {
			return Response.error(new ParseError(e));
		}
	}
}
