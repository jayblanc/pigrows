<#function max x y>
    <#if (x<y)><#return y><#else><#return x></#if>
</#function>

<#function min x y>
    <#if (x<y)><#return x><#else><#return y></#if>
</#function>

<#macro pages size p base>
    <#if (p<=4)> 
        <#assign interval = 1..(min(5,size))>
    <#elseif ((size-p)<4)> 
        <#assign interval = (max(1,(size-4)))..size >
    <#else>
        <#assign interval = (p-2)..(p+2)>
    </#if>
    <ul class="pagination">
    	<#if page gt 1>
	     	<li><a href="${base}?p=1" >first</a></li>
	     	<li>
		    	<a href="${base}?p=${page-1}" aria-label="Previous">
					<span aria-hidden="true">&laquo;</span>
				</a>
			</li>
	    <#else>
	    	<li class="disabled"><a href="${base}?p=1" >first</a></li>
	    	<li class="disabled">
		    	<a href="${base}?p=${page-1}" aria-label="Previous">
					<span aria-hidden="true">&laquo;</span>
				</a>
			</li>
	    </#if>
	    <#list interval as page>
	        <#if page=p>
	        	<li class="active"><a href="${base}?p=${page}" >${page}</a></li>
	        <#else>
	        	<li><a href="${base}?p=${page}" >${page}</a></li>
	        </#if>
	    </#list>
	    <#if page lt size>
		    <li>
				<a href="${base}?p=${page+1}" aria-label="Next">
					<span aria-hidden="true">&raquo;</span>
				</a>
			</li>
	    	<li><a href="${base}?p=${size}" >last</a></li>
	    <#else>
	    	<li class="disabled">
				<a href="${base}?p=${page+1}" aria-label="Next">
					<span aria-hidden="true">&raquo;</span>
				</a>
			</li>
	    	<li class="disabled"><a href="${base}?p=${size}" >last</a></li>
	    </#if>
</#macro>
