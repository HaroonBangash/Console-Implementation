"use client";

import * as React from "react";
import { Copy } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useToast } from "@/components/ui/use-toast";

type CopyFieldProps = {
  value: string;
  label?: string;
};

export function CopyField({ value, label }: CopyFieldProps) {
  const { addToast } = useToast();
  const handleCopy = React.useCallback(async () => {
    await navigator.clipboard.writeText(value);
    addToast({ title: "Copied", description: label ?? "Link copied to clipboard." });
  }, [addToast, label, value]);

  return (
    <div className="flex items-center gap-2">
      <Input value={value} readOnly aria-label={label ?? "Copy value"} />
      <Button type="button" onClick={handleCopy} variant="secondary" className="shrink-0" aria-label="Copy value">
        <Copy className="mr-2 h-4 w-4" /> Copy
      </Button>
    </div>
  );
}
