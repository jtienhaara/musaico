
                // Now also create "." and ".." entries for the new
                // child hierarchical object.
                OEntry dot = new SimpleOEntry ( this.module,
                                                child_entry,
                                                ".",
                                                child_entry.superBlockRef (),
                                                child_entry.onodeRef (),
                                                child_entry.isMounted () );
                this.link ( dot,
                            credentials,
                            progress,
                            child_entry.onodeRef () );

                OEntry dot_dot = new SimpleOEntry ( this.module,
                                                    entry,
                                                    "..",
                                                    entry.superBlockRef (),
                                                    entry.onodeRef (),
                                                    entry.isMounted () );
                this.link ( dot_dot,
                            credentials,
                            progress,
                            entry.onodeRef () );
