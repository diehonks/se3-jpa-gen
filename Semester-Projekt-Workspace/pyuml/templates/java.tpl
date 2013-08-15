%if e.__class__.__name__ == 'JClass':
  %include class cls=e
%elif e.__class__.__name__ == 'JEnum':
  %include enum enum=e
%elif e.__class__.__name__ == 'JInterface':
  %include interface itf=e
%else:
  kartoffelchips!
%end
