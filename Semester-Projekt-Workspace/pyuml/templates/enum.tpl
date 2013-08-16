package {{enum.package}};

public enum {{enum.name}} {
	\\
%for l in enum.literals:
{{l}}, \\
%end

}
